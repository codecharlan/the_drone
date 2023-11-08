package com.codecharlan.thedrone.service.service_impl;

import com.codecharlan.thedrone.dto.request.DroneRequest;
import com.codecharlan.thedrone.dto.response.*;
import com.codecharlan.thedrone.entity.Drone;
import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import com.codecharlan.thedrone.exception.*;
import com.codecharlan.thedrone.repository.DroneRepository;
import com.codecharlan.thedrone.service.DroneService;
import com.codecharlan.thedrone.utils.ImageEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codecharlan.thedrone.enums.DroneState.DELIVERED;
import static com.codecharlan.thedrone.enums.DroneState.IDLE;
import static com.codecharlan.thedrone.enums.DroneState.RETURNING;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Slf4j
@Service
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final LogService logService;

    @Value("${drone.low_battery_threshold}")
    private int LOW_BATTERY_THRESHOLD;

    @Value("${drone.battery_increment_idle}")
    private double BATTERY_INCREMENT_IDLE;

    @Value("${drone.battery_decrement_loading}")
    private double BATTERY_DECREMENT_LOADING;

    @Value("${drone.battery_decrement_loaded}")
    private double BATTERY_DECREMENT_LOADED;

    @Value("${drone.battery_decrement_delivering}")
    private double BATTERY_DECREMENT_DELIVERING;

    @Value("${drone.battery_decrement_delivered}")
    private double BATTERY_DECREMENT_DELIVERED;

    @Value("${drone.battery_decrement_returning}")
    private double BATTERY_DECREMENT_RETURNING;


    @Override
    public ApiResponse<DroneResponse> registerDrone(DroneRequest newDrone) {
        validateDroneRegistrationLimit();
        validateDroneNotRegistered(newDrone.getSerialNumber());

        Drone newDbDrone = createNewDrone(newDrone);
        Drone savedDB = droneRepository.save(newDbDrone);
        DroneResponse response = createDroneResponse(savedDB);
        return new ApiResponse<>("Drone with serial Number: " + response.getSerialNumber() + " Registered Successfully", response, CREATED.value());
    }

    private void validateDroneRegistrationLimit() {
        int MAX_DRONE_REGISTRATION = 10;
        if (getAllDrones().data().size() >= MAX_DRONE_REGISTRATION) {
            throw new RegisterDroneLimitExceededException("You can only register " + MAX_DRONE_REGISTRATION + " drones");
        }
    }

    private void validateDroneNotRegistered(String serialNumber) {
        Optional<Drone> droneCheck = droneRepository.findBySerialNumber(serialNumber);
        if (droneCheck.isPresent()) {
            throw new DroneAlreadyRegisteredException("Drone already registered with " + droneCheck.get().getSerialNumber());
        }
    }

    private Drone createNewDrone(DroneRequest newDrone) {
        return Drone.builder()
                .serialNumber(newDrone.getSerialNumber())
                .model(newDrone.getModel())
                .weightLimit(calculateWeightLimit(newDrone.getModel()))
                .loadedMedications(null)
                .build();
    }

    protected Double calculateWeightLimit(DroneModel model) {
        return model.getWeightLimit();
    }

    private DroneResponse createDroneResponse(Drone drone) {
        return DroneResponse.builder()
                .serialNumber(drone.getSerialNumber())
                .state(drone.getState())
                .weightLimit(drone.getWeightLimit())
                .batteryLevel(drone.getBatteryLevel())
                .model(drone.getModel())
                .loadedMedications(drone.getLoadedMedications())
                .build();
    }

    @Override
    public ApiResponse<List<DroneResponse>> getAllDrones() {
        List<Drone> allDrones = droneRepository.findAll();
        List<DroneResponse> responses = allDrones.stream()
                .map(this::createDroneResponse)
                .collect(Collectors.toList());
        return new ApiResponse<>(responses.size() + " Drones loaded successfully", responses, OK.value());
    }

    @Override
    public ApiResponse<DroneResponse> loadMedicationsOntoDrone(String serialNumber, List<Medication> medications) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        validateDroneLoadability(drone, medications);
        updateDroneStateAndBattery(drone, medications);
        return new ApiResponse<>("Drone with serial Number: " + drone.getSerialNumber() + " Has been Loaded Successfully", createDroneResponse(drone), OK.value());
    }

    private Drone getDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber).orElseThrow(() -> new DroneNotFoundException("Drone not found"));
    }

    private void validateDroneLoadability(Drone drone, List<Medication> medications) {
        if (drone.getState() != IDLE) {
            throw new DroneCanNotBeLoadedException("Drone is not currently available for loading");
        }
        if (drone.getBatteryLevel() < LOW_BATTERY_THRESHOLD) {
            drone.setState(RETURNING);
            throw new DroneBatteryLowException("Drone battery is low, Returning to Base");
        }

        double totalWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        double weightLimit = getWeightLimitForModel(drone.getModel());

        if (totalWeight > weightLimit) {
            throw new DroneOverLoadedException("Drone has exceeded its weight limit, reduce the medication");
        }
    }

    double getWeightLimitForModel(DroneModel model) {
        return switch (model) {
            case Lightweight -> 100.00;
            case Middleweight -> 200.00;
            case Cruiserweight -> 300.00;
            case Heavyweight -> 500.00;
            default -> throw new RuntimeException("Unknown drone model: " + model);
        };
    }

    private void updateDroneStateAndBattery(Drone drone, List<Medication> medications) {
        drone.setState(DroneState.LOADING);

        for (Medication medication : medications) {
            drone.addLoadedMedication(medication);
            if (medication.getImage() != null) {
                medication.setImage(ImageEncoder.encodeImageToBase64(medication.getImage()).getBytes());
            }
        }

        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);
        updateBatteryLevel(drone);
    }
    @Override
    public ApiResponse<DroneBatteryResponse> getDroneBatteryLevel(String serialNumber) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        DroneBatteryResponse batteryLevel = DroneBatteryResponse.builder()
                .serialNumber(drone.getSerialNumber())
                .batteryLevel(drone.getBatteryLevel())
                .build();
        return new ApiResponse<>("Drone Battery Level Loaded Successfully", batteryLevel, OK.value());
    }

    @Override
    public ApiResponse<List<DroneResponse>> getAvailableDrones() {
        List<Drone> idleDrones = droneRepository.findAllByState(IDLE);
        List<DroneResponse> availableDrones = idleDrones.stream()
                .map(this::createDroneResponse)
                .toList();
        return new ApiResponse<>("All Available Drones Loaded Successfully", availableDrones, OK.value());
    }

    @Override
    public ApiResponse<DroneLoadedMedicResponse> getLoadedMedications(String serialNumber) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        Set<Medication> loadedMedications = drone.getLoadedMedications();
        Set<MedicationResponse> loadedMedicationsResponse = loadedMedications.stream()
                .map(medication -> {
                    MedicationResponse medicationResponse = new MedicationResponse();
                    medicationResponse.setName(medication.getName());
                    medicationResponse.setCode(medication.getCode());
                    medicationResponse.setWeight(medication.getWeight());
                    medicationResponse.setImage((medication.getImage()));
                    return medicationResponse;
                })
                .collect(Collectors.toSet());
        DroneLoadedMedicResponse response = DroneLoadedMedicResponse.builder()
                .serialNumber(drone.getSerialNumber())
                .loadedMedications(loadedMedicationsResponse)
                .build();

        return new ApiResponse<>("Loaded Medications for Drone with serial Number: " + drone.getSerialNumber(), response, OK.value());
    }

    @Scheduled(fixedRate = 12000)
    @Transactional
    public void checkAndUpdateBatteryLevels() {
        List<Drone> drones = droneRepository.findAll();
        for (Drone drone : drones) {
            updateBatteryLevel(drone);
            if (drone.getBatteryLevel() < LOW_BATTERY_THRESHOLD && !(drone.getState() == IDLE)) {
                drone.setState(RETURNING);
                droneRepository.save(drone);
                logService.logAuditEvent("Drone battery is low, Returning to base:: [{}]", new DroneLogResponse(drone.getSerialNumber(), drone.getBatteryLevel() + "%"));
            }
        }

    }
    @Transactional
    public void updateBatteryLevel(Drone drone) {
        getDroneBySerialNumber(drone.getSerialNumber());
        Double batteryLevel = drone.getBatteryLevel();
        if (batteryLevel != null) {
            logService.logAuditEvent("Drone battery level checked :: [{}]", new DroneLogResponse(drone.getSerialNumber(), batteryLevel + "%"));
            switch (drone.getState()) {
                case IDLE -> batteryLevel = Math.min(100, batteryLevel + BATTERY_INCREMENT_IDLE);
                case LOADING -> batteryLevel = Math.max(0, batteryLevel - BATTERY_DECREMENT_LOADING);
                case LOADED -> batteryLevel = Math.max(0, batteryLevel - BATTERY_DECREMENT_LOADED);
                case DELIVERING -> batteryLevel = Math.max(0, batteryLevel - BATTERY_DECREMENT_DELIVERING);
                case DELIVERED -> batteryLevel = Math.max(0, batteryLevel - BATTERY_DECREMENT_DELIVERED);
                case RETURNING -> batteryLevel = Math.max(0, batteryLevel - BATTERY_DECREMENT_RETURNING);
                default -> {
                    log.warn("Unrecognized drone state: " + drone.getState());
                    logService.logAuditEvent("Unrecognized drone state:: [{}]", new DroneLogResponse(drone.getSerialNumber(), batteryLevel + "%"));
                }
            }

            drone.setBatteryLevel(batteryLevel);
            if (drone.getState() == DELIVERED) {
                for (Medication medication : drone.getLoadedMedications()) {
                    medication.setDrone(null);
                }
                droneRepository.save(drone);
            }

            if(drone.getBatteryLevel() == 0.0){
                drone.setState(IDLE);
                droneRepository.save(drone);
            }
            droneRepository.save(drone);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkStateAndUpdate() {
        List<Drone> allDrones = droneRepository.findAll();
        for (Drone drone : allDrones) {
            updateDroneState(drone);
        }
    }

    private void updateDroneState(Drone drone) {
        switch (drone.getState()) {
            case RETURNING -> drone.setState(DroneState.IDLE);
            case LOADED -> drone.setState(DroneState.DELIVERING);
            case DELIVERING -> drone.setState(DELIVERED);
            case DELIVERED -> drone.setState(DroneState.RETURNING);
            default -> {
            }
        }
        droneRepository.save(drone);
    }
}

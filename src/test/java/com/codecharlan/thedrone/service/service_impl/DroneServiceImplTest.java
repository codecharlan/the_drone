package com.codecharlan.thedrone.service.service_impl;

import com.codecharlan.thedrone.dto.request.DroneRequest;
import com.codecharlan.thedrone.dto.response.ApiResponse;
import com.codecharlan.thedrone.dto.response.DroneBatteryResponse;
import com.codecharlan.thedrone.dto.response.DroneLoadedMedicResponse;
import com.codecharlan.thedrone.dto.response.DroneResponse;
import com.codecharlan.thedrone.dto.response.MedicationResponse;
import com.codecharlan.thedrone.entity.Drone;
import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import com.codecharlan.thedrone.exception.*;
import com.codecharlan.thedrone.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codecharlan.thedrone.enums.DroneModel.Cruiserweight;
import static com.codecharlan.thedrone.enums.DroneModel.Heavyweight;
import static com.codecharlan.thedrone.enums.DroneModel.Lightweight;
import static com.codecharlan.thedrone.enums.DroneModel.Middleweight;
import static com.codecharlan.thedrone.enums.DroneState.IDLE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {
    @Mock
    private DroneRepository droneRepository;
    @InjectMocks
    private DroneServiceImpl droneService;
    @Mock
    private LogService logService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        droneService = new DroneServiceImpl(droneRepository, logService);
    }
    @Test
    public void shouldRegisterDrone() {
        DroneRequest newDrone = new DroneRequest();
        newDrone.setSerialNumber("1234567890");
        newDrone.setModel(Lightweight);
        Mockito.when(droneRepository.save(any(Drone.class))).thenReturn(new Drone());
        ApiResponse<DroneResponse> response = droneService.registerDrone(newDrone);
        assertNotNull(response);
        assertEquals(CREATED.value(), response.status());
        assertEquals(100, response.data().getBatteryLevel());
        assertEquals(IDLE, response.data().getState());
    }
    @Test
    public void shouldThrowExceptionWhenRegisteringAlreadyRegisteredDrone() {
        DroneRequest newDrone = new DroneRequest();
        newDrone.setSerialNumber("1234567890");
        newDrone.setModel(Lightweight);
        Mockito.when(droneRepository.findBySerialNumber("1234567890")).thenReturn(Optional.of(new Drone()));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneService.registerDrone(newDrone));
    }
    @Test
    public void shouldThrowExceptionWhenRegisteringMoreThan10Drones() {
        DroneRequest newDrone = new DroneRequest();
        newDrone.setSerialNumber("1234567890");
        newDrone.setModel(Lightweight);
        Mockito.when(droneRepository.findAll()).thenReturn(Collections.nCopies(10, new Drone()));
        assertThrows(RegisterDroneLimitExceededException.class, () -> droneService.registerDrone(newDrone));
    }
    @Test
    public void shouldGetAllDrones() {
        Mockito.when(droneRepository.findAll()).thenReturn(Collections.nCopies(10, new Drone()));
        ApiResponse<List<DroneResponse>> response = droneService.getAllDrones();
        assertEquals(OK.value(), response.status());
        assertEquals(10, response.data().size());
    }

    @Test
    public void shouldThrowExceptionWhenLoadingMedicationsOntoADroneThatIsNotInIdleState() {
        String serialNumber = "1234567890";
        List<Medication> medications = Collections.singletonList(new Medication());
        Drone drone = new Drone();
        drone.setState(DroneState.LOADED);
        Mockito.when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        assertThrows(DroneCanNotBeLoadedException.class, () -> droneService.loadMedicationsOntoDrone(serialNumber, medications));
    }
    @Test
    public void shouldGetLoadedMedications() {
        Drone drone = new Drone();
        drone.setSerialNumber("12345");

        Medication medication1 = new Medication(1L, "Medication_1", 10.0, "CODE", new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10}, new Drone());
        Medication medication2 = new Medication(2L,"Medication_2", 10.0, "CODE2", new byte[] {(byte) 137, 88, 78, 71, 13, 10, 22, 10}, new Drone());

        Set<Medication> medications = new HashSet<>();
        medications.add(medication1);
        medications.add(medication2);
        drone.setLoadedMedications(medications);

        Mockito.when(droneRepository.findBySerialNumber("12345")).thenReturn(Optional.of(drone));

        ApiResponse<DroneLoadedMedicResponse> result = droneService.getLoadedMedications("12345");
        assertEquals("Loaded Medications for Drone with serial Number: 12345", result.message());
        assertEquals(2, result.data().getLoadedMedications().size());
        Set<MedicationResponse> expectedMedicationResponses = medications.stream()
                .map(medication -> {
                    MedicationResponse medicationResponse = new MedicationResponse();
                    medicationResponse.setName(medication.getName());
                    medicationResponse.setCode(medication.getCode());
                    medicationResponse.setWeight(medication.getWeight());
                    medicationResponse.setImage(medication.getImage());
                    return medicationResponse;
                })
                .collect(Collectors.toSet());
        Set<MedicationResponse> actualMedicationResponses = result.data().getLoadedMedications();
        assertEquals(expectedMedicationResponses, actualMedicationResponses);
    }
    @Test
    public void shouldGetWeightLimitForModel() {
        double lightweightLimit = droneService.getWeightLimitForModel(Lightweight);
        assertEquals(100, lightweightLimit);
        double middleweightLimit = droneService.getWeightLimitForModel(Middleweight);
        assertEquals(200, middleweightLimit);
        double cruiserweightLimit = droneService.getWeightLimitForModel(Cruiserweight);
        assertEquals(300, cruiserweightLimit);
        double heavyweightLimit = droneService.getWeightLimitForModel(Heavyweight);
        assertEquals(500, heavyweightLimit);
        assertThrows(RuntimeException.class, () -> droneService.getWeightLimitForModel(null));
    }

    @Test
    public void shouldThrowExceptionWhenGettingLoadedMedicationsFromADroneThatDoesNotExist() {
        String serialNumber = "1234567890";
        Mockito.when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        assertThrows(DroneNotFoundException.class, () -> droneService.getLoadedMedications(serialNumber));
    }


    @Test
    public void shouldGetAvailableDrones() {
        List<Drone> idleDrones = Collections.singletonList(new Drone());
        Mockito.when(droneRepository.findAllByState(IDLE)).thenReturn(idleDrones);
        ApiResponse<List<DroneResponse>> response = droneService.getAvailableDrones();
        assertEquals(OK.value(), response.status());
        assertEquals(1, response.data().size());
    }

    @Test
    public void shouldCalculateWeightLimitForModel() {
        Double lightweightLimit = droneService.calculateWeightLimit(Lightweight);
        assertEquals(Lightweight.getWeightLimit(), lightweightLimit);
        Double middleweightLimit = droneService.calculateWeightLimit(Middleweight);
        assertEquals(Middleweight.getWeightLimit(), middleweightLimit);
        Double cruiserweightLimit = droneService.calculateWeightLimit(Cruiserweight);
        assertEquals(Cruiserweight.getWeightLimit(), cruiserweightLimit);
        Double heavyweightLimit = droneService.calculateWeightLimit(Heavyweight);
        assertEquals(Heavyweight.getWeightLimit(), heavyweightLimit);
    }

    @Test
    public void shouldGetDroneBatteryLevel() {
        String serialNumber = "1234567890";
        Double batteryLevel = 50.00;

        Drone drone = new Drone();
        drone.setBatteryLevel(batteryLevel);
        Mockito.when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        ApiResponse<DroneBatteryResponse> response = droneService.getDroneBatteryLevel(serialNumber);
        assertEquals(OK.value(), response.status());
        assertEquals(batteryLevel, response.data().getBatteryLevel());
    }

    @Test
    public void shouldThrowExceptionWhenGettingBatteryLevelOfADroneThatDoesNotExist() {
        String serialNumber = "1234567890";
        Mockito.when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());
        assertThrows(DroneNotFoundException.class, () -> droneService.getDroneBatteryLevel(serialNumber));
    }

    @Test
    public void shouldUpdateBatteryLevel() {
        String serialNumber = "DRONE-3456";
        double batteryLevel = 50.00;

        Drone drone = new Drone();
        drone.setId(1L);
        drone.setModel(DroneModel.Lightweight);
        drone.setWeightLimit(100.0);
        drone.setSerialNumber(serialNumber);
        drone.setBatteryLevel(batteryLevel);
        drone.setState(DroneState.LOADED);

        Mockito.when(droneRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.of(drone));
        droneService.updateBatteryLevel(drone);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingBatteryLevelOfADroneThatDoesNotExist() {
        Drone drone = new Drone();
        assertThrows(DroneNotFoundException.class, () -> droneService.updateBatteryLevel(drone));
    }
}
package com.codecharlan.thedrone.dispatch_controller;

import com.codecharlan.thedrone.dto.request.DroneRequest;
import com.codecharlan.thedrone.dto.response.ApiResponse;
import com.codecharlan.thedrone.dto.response.DroneBatteryResponse;
import com.codecharlan.thedrone.dto.response.DroneLoadedMedicResponse;
import com.codecharlan.thedrone.dto.response.DroneResponse;
import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drones")
public class DroneController {

    private final DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<DroneResponse>> registerDrone(@Valid @RequestBody DroneRequest newDrone) {
        ApiResponse<DroneResponse> response = droneService.registerDrone(newDrone);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/all-drones")
    public ResponseEntity<ApiResponse<List<DroneResponse>>> getAllDrones() {
        ApiResponse<List<DroneResponse>> response = droneService.getAllDrones();
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping("/load-medic/{serialNumber}")
    public ResponseEntity<ApiResponse<DroneResponse>> loadMedicationsOntoDrone(
            @Valid
            @PathVariable String serialNumber,
            @RequestBody List<Medication> medications
    ) throws IOException {
        ApiResponse<DroneResponse> response = droneService.loadMedicationsOntoDrone(serialNumber, medications);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @GetMapping("/loaded-medic/{serialNumber}")
    public ResponseEntity<ApiResponse<DroneLoadedMedicResponse>> getLoadedMedications(@Valid @PathVariable String serialNumber) {
        ApiResponse<DroneLoadedMedicResponse> response = droneService.getLoadedMedications(serialNumber);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<DroneResponse>>> getAvailableDrones() {
        ApiResponse<List<DroneResponse>> response = droneService.getAvailableDrones();
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/battery-level/{serialNumber}")
    public ResponseEntity<ApiResponse<DroneBatteryResponse>> getDroneBatteryLevel(@Valid @PathVariable String serialNumber) {
        ApiResponse<DroneBatteryResponse> response = droneService.getDroneBatteryLevel(serialNumber);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}

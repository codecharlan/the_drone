package com.codecharlan.thedrone.dispatch_controller;

import com.codecharlan.thedrone.dto.request.DroneRequest;
import com.codecharlan.thedrone.dto.response.ApiResponse;
import com.codecharlan.thedrone.dto.response.DroneBatteryResponse;
import com.codecharlan.thedrone.dto.response.DroneLoadedMedicResponse;
import com.codecharlan.thedrone.dto.response.DroneResponse;
import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class DroneControllerTest {

    private DroneController droneController;
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        droneService = mock(DroneService.class);
        droneController = new DroneController(droneService);
    }

    @Test
    void shouldRegisterDrone() {
        DroneRequest newDrone = new DroneRequest();
        ApiResponse<DroneResponse> response = new ApiResponse<>("Drone registered", new DroneResponse(), HttpStatus.CREATED.value());

        Mockito.when(droneService.registerDrone(newDrone)).thenReturn(response);

        ResponseEntity<ApiResponse<DroneResponse>> result = droneController.registerDrone(newDrone);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAllDrones() {
        ApiResponse<List<DroneResponse> > response = new ApiResponse<>("Drones retrieved", Collections.emptyList(), HttpStatus.OK.value());

        Mockito.when(droneService.getAllDrones()).thenReturn(response);

        ResponseEntity<ApiResponse<List<DroneResponse>> > result = droneController.getAllDrones();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldLoadMedicationsOntoDrone() throws IOException {
        String serialNumber = "1234567890";
        List<Medication> medications = Collections.singletonList(new Medication());
        ApiResponse<DroneResponse> response = new ApiResponse<>("Medications loaded", new DroneResponse(), HttpStatus.OK.value());

        Mockito.when(droneService.loadMedicationsOntoDrone(serialNumber, medications)).thenReturn(response);

        ResponseEntity<ApiResponse<DroneResponse>> result = droneController.loadMedicationsOntoDrone(serialNumber, medications);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetLoadedMedications() {
        String serialNumber = "1234567890";
        ApiResponse<DroneLoadedMedicResponse> response = new ApiResponse<>("Loaded medications retrieved", new DroneLoadedMedicResponse(), HttpStatus.OK.value());

        Mockito.when(droneService.getLoadedMedications(serialNumber)).thenReturn(response);

        ResponseEntity<ApiResponse<DroneLoadedMedicResponse>> result = droneController.getLoadedMedications(serialNumber);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetAvailableDrones() {
        ApiResponse<List<DroneResponse> > response = new ApiResponse<>("Available drones retrieved", Collections.emptyList(), HttpStatus.OK.value());

        Mockito.when(droneService.getAvailableDrones()).thenReturn(response);

        ResponseEntity<ApiResponse<List<DroneResponse>> > result = droneController.getAvailableDrones();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void shouldGetDroneBatteryLevel() {
        String serialNumber = "1234567890";
        ApiResponse<DroneBatteryResponse> response = new ApiResponse<>("Battery level retrieved", new DroneBatteryResponse(), HttpStatus.OK.value());

        Mockito.when(droneService.getDroneBatteryLevel(serialNumber)).thenReturn(response);

        ResponseEntity<ApiResponse<DroneBatteryResponse>> result = droneController.getDroneBatteryLevel(serialNumber);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}

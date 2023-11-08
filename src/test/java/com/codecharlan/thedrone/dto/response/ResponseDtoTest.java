package com.codecharlan.thedrone.dto.response;

import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResponseDtoTest {
    @Test
    public void testApiResponse() {
        ApiResponse<String> response = new ApiResponse<>("Success", "Sample Data", 200);

        assertEquals("Success", response.message());
        assertEquals("Sample Data", response.data());
        assertEquals(200, response.status());
    }

    @Test
    public void testApiResponseWithDifferentDataTypes() {
        ApiResponse<Integer> response = new ApiResponse<>("Success", 42, 200);

        assertEquals("Success", response.message());
        assertEquals(42, response.data());
        assertEquals(200, response.status());
    }

    @Test
    public void testApiResponseWithNullData() {
        ApiResponse<String> response = new ApiResponse<>("Success", null, 200);

        assertEquals("Success", response.message());
        assertNull(response.data());
        assertEquals(200, response.status());
    }

    @Test
    public void testDroneLogResponse() {
        DroneLogResponse response = DroneLogResponse.droneBuilder("000123", "80%").build();

        assertEquals("000123", response.getSerialNumber());
        assertEquals("80%", response.getBatteryLevel());
    }

    @Test
    public void testDroneLogResponseWithDifferentData() {
        DroneLogResponse response = DroneLogResponse.droneBuilder("000456", "60%").build();

        assertEquals("000456", response.getSerialNumber());
        assertEquals("60%", response.getBatteryLevel());
    }

    @Test
    public void testDroneResponse() {
        Set<Medication> loadedMedications = new HashSet<>();
        loadedMedications.add(new Medication(1L, "Medication1", 5.00, "Code1", new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10}, null));

        DroneResponse response = DroneResponse.builder()
                .serialNumber("000123")
                .model(DroneModel.Lightweight)
                .weightLimit(100.00)
                .batteryLevel(80.00)
                .state(DroneState.IDLE)
                .loadedMedications(loadedMedications)
                .build();
        assertEquals("000123", response.getSerialNumber());
        assertEquals(DroneModel.Lightweight, response.getModel());
        assertEquals(100.00, response.getWeightLimit());
        assertEquals(80.00, response.getBatteryLevel());
        assertEquals(DroneState.IDLE, response.getState());
        assertEquals(loadedMedications, response.getLoadedMedications());
    }

    @Test
    public void testDroneResponseWithDifferentData() {
        Set<Medication> loadedMedications = new HashSet<>();
        loadedMedications.add(new Medication(2L, "Medication2", 10.00, "Code2", new byte[] {(byte) 137, 80, 78, 77, 13, 10, 27, 10}, null));

        DroneResponse response = DroneResponse.builder()
                .serialNumber("000456")
                .model(DroneModel.Middleweight)
                .weightLimit(200.00)
                .batteryLevel(60.00)
                .state(DroneState.LOADED)
                .loadedMedications(loadedMedications)
                .build();

        assertEquals("000456", response.getSerialNumber());
        assertEquals(DroneModel.Middleweight, response.getModel());
        assertEquals(200.00, response.getWeightLimit());
        assertEquals(60.00, response.getBatteryLevel());
        assertEquals(DroneState.LOADED, response.getState());
        assertEquals(loadedMedications, response.getLoadedMedications());
    }

    @Test
    public void testCreateDroneBatteryResponse() {
        String serialNumber = "000123";
        Double batteryLevel = 75.00;
        DroneBatteryResponse response = new DroneBatteryResponse(serialNumber, batteryLevel);
        assertEquals(serialNumber, response.getSerialNumber());
        assertEquals(batteryLevel, response.getBatteryLevel());
    }

    @Test
    public void testToString() {
        String serialNumber = "000123";
        Double batteryLevel = 75.02;
        DroneBatteryResponse response = new DroneBatteryResponse(serialNumber, batteryLevel);
        String expectedToString = "DroneBatteryResponse(serialNumber=000123, batteryLevel=75.02)";
        assertEquals(expectedToString, response.toString());
    }
    @Test
    public void testSerialNumber() {
        Set<MedicationResponse> loadedMedications = new HashSet<>();
        MedicationResponse medication1 = new MedicationResponse("Medication_1", 10.0, "CODE", new byte[] {(byte) 17, 80, 78, 71, 13, 10, 26, 11});
        MedicationResponse medication2 = new MedicationResponse("Medication_2", 17.0, "CODE-07", new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10});
        loadedMedications.add(medication1);
        loadedMedications.add(medication2);

        DroneLoadedMedicResponse droneLoadedMedicResponse = DroneLoadedMedicResponse.builder()
                .serialNumber("12345")
                .loadedMedications(loadedMedications)
                .build();
        assertEquals("12345", droneLoadedMedicResponse.getSerialNumber());
    }
    @Test
    public void testLoadedMedications() {
        Set<MedicationResponse> loadedMedications = new HashSet<>();
        MedicationResponse medication1 = new MedicationResponse("Medication_1", 10.0, "CODE", new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10});
        MedicationResponse medication2 = new MedicationResponse("Medication_2", 17.0, "CODE-07", new byte[] {(byte) 137, 50, 18, 71, 13, 10, 26, 10});
        loadedMedications.add(medication1);
        loadedMedications.add(medication2);

        DroneLoadedMedicResponse droneLoadedMedicResponse = DroneLoadedMedicResponse.builder()
                .serialNumber("12345")
                .loadedMedications(loadedMedications)
                .build();
        Set<MedicationResponse> loadMedications = droneLoadedMedicResponse.getLoadedMedications();
        assertEquals(2, loadMedications.size());
    }
}

package com.codecharlan.thedrone.dto.request;
import com.codecharlan.thedrone.enums.DroneModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DroneRequestTest {

    @Test
    public void testDroneRequest() {
        DroneRequest request = DroneRequest.builder()
                .serialNumber("000123")
                .model(DroneModel.Lightweight)
                .build();
        assertEquals("000123", request.getSerialNumber());
        assertEquals(DroneModel.Lightweight, request.getModel());
    }
    @Test
    public void testDroneRequestWithDifferentData() {
        DroneRequest request = DroneRequest.builder()
                .serialNumber("000456")
                .model(DroneModel.Middleweight)
                .build();
        assertEquals("000456", request.getSerialNumber());
        assertEquals(DroneModel.Middleweight, request.getModel());
    }
}

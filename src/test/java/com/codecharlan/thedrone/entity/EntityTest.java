package com.codecharlan.thedrone.entity;

import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EntityTest {

    @Test
    public void testDroneEntity() {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setSerialNumber("serial123");
        drone.setModel(DroneModel.Lightweight);
        drone.setWeightLimit(100.00);
        drone.setBatteryLevel(80.00);
        drone.setState(DroneState.LOADING);

        assertEquals(1L, drone.getId());
        assertEquals("serial123", drone.getSerialNumber());
        assertEquals(DroneModel.Lightweight, drone.getModel());
        assertEquals(100.00, drone.getWeightLimit());
        assertEquals(80.00, drone.getBatteryLevel());
        assertEquals(DroneState.LOADING, drone.getState());
        assertNull(drone.getLoadedMedications());

    }

    @Test
    public void testMedicationEntity() {
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Medicine1");
        medication.setWeight(50.00);
        medication.setCode("code123");
        medication.setImage(new byte[] {(byte) 137, 80, 78, 71, 13, 10, 26, 10});

        assertEquals(1L, medication.getId());
        assertEquals("Medicine1", medication.getName());
        assertEquals(50.00, medication.getWeight());
        assertEquals("code123", medication.getCode());

        Drone drone = new Drone();
        medication.setDrone(drone);
        assertEquals(drone, medication.getDrone());
    }
}

package com.codecharlan.thedrone.repository;

import com.codecharlan.thedrone.entity.Drone;
import com.codecharlan.thedrone.enums.DroneState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DroneRepositoryTest {

    @Autowired
    private DroneRepository droneRepository;

    @Test
    public void testFindBySerialNumber() {
        Drone drone = new Drone();
        drone.setSerialNumber("000123");
        droneRepository.save(drone);
        Optional<Drone> foundDrone = droneRepository.findBySerialNumber("000123");

        assertTrue(foundDrone.isPresent());
        assertEquals("000123", foundDrone.get().getSerialNumber());
    }

    @Test
    public void testFindAllByState() {
        Drone drone1 = new Drone();
        drone1.setState(DroneState.IDLE);
        droneRepository.save(drone1);

        Drone drone2 = new Drone();
        drone2.setState(DroneState.LOADING);
        droneRepository.save(drone2);

        List<Drone> dronesWithStateIdle = droneRepository.findAllByState(DroneState.IDLE);
        List<Drone> dronesWithStateLoading = droneRepository.findAllByState(DroneState.LOADING);

        assertEquals(1, dronesWithStateIdle.size());
        assertEquals(1, dronesWithStateLoading.size());
    }
}

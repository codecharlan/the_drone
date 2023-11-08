package com.codecharlan.thedrone.repository;

import com.codecharlan.thedrone.entity.Drone;
import com.codecharlan.thedrone.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
    List<Drone> findAllByState(DroneState state);

}

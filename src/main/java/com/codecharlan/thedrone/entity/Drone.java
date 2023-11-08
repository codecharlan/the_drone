package com.codecharlan.thedrone.entity;

import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private Double weightLimit;

    @Builder.Default
    @Max(value = 100, message = "BatteryLevel Cannot exceed 100%")
    private Double batteryLevel = 100.00;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DroneState state = DroneState.IDLE;

    @JsonIgnore
    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Medication> loadedMedications;
    public void addLoadedMedication(Medication medication) {
        loadedMedications.add(medication);
        medication.setDrone(this);
    }
}

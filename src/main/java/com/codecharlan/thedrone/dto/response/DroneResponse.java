package com.codecharlan.thedrone.dto.response;

import com.codecharlan.thedrone.entity.Medication;
import com.codecharlan.thedrone.enums.DroneModel;
import com.codecharlan.thedrone.enums.DroneState;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@JsonSerialize
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DroneResponse {
    private String serialNumber;

    private DroneModel model;

    private Double weightLimit;

    private Double batteryLevel;

    private DroneState state;

    private Set<Medication> loadedMedications;

}

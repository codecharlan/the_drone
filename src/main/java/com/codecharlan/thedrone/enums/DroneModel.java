package com.codecharlan.thedrone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DroneModel {
    Lightweight(100.00),
    Middleweight(200.00),
    Cruiserweight(300.00),
    Heavyweight(500.00);

    private final double weightLimit;

}

package com.codecharlan.thedrone.dto.request;
import com.codecharlan.thedrone.enums.DroneModel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DroneRequest {
    private String serialNumber;
    private DroneModel model;
}

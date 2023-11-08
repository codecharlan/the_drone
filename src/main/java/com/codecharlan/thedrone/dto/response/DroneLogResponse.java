package com.codecharlan.thedrone.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@Setter
@Getter
public class DroneLogResponse {
    private String serialNumber;
    private String batteryLevel;
    public static DroneLogResponseBuilder droneBuilder(String serialNumber, String batteryLevel) {
        return builder().serialNumber(serialNumber).batteryLevel(batteryLevel);
    }
}

package com.codecharlan.thedrone.service.service_impl;

import com.codecharlan.thedrone.dto.response.DroneLogResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger("DRONE_BATTERY_LEVEL_AUDIT");
    public void logAuditEvent(String message, DroneLogResponse droneLog) {
        logger.info(message, droneLog);
    }
}


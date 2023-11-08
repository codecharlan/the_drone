package com.codecharlan.thedrone.exception;

public class DroneBatteryLowException extends RuntimeException {
    public DroneBatteryLowException(String s) {
        super(s);
    }
}

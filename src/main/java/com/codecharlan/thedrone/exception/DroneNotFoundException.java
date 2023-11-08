package com.codecharlan.thedrone.exception;

public class DroneNotFoundException extends RuntimeException {
    public DroneNotFoundException(String s) {
        super(s);
    }
}

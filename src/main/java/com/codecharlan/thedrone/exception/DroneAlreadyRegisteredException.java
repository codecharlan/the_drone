package com.codecharlan.thedrone.exception;

public class DroneAlreadyRegisteredException extends RuntimeException {
    public DroneAlreadyRegisteredException(String s) {
        super(s);
    }
}

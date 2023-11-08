package com.codecharlan.thedrone.exception;

import com.codecharlan.thedrone.dto.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;
    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }
    @Test
    void testHandleDroneOverLoadedException() {
        DroneOverLoadedException exception = new DroneOverLoadedException("Drone overloaded");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleDroneOverLoadedException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Drone overloaded", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleDroneNotFoundException() {
        DroneNotFoundException exception = new DroneNotFoundException("Drone not found");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleDroneNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Drone not found", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleDroneBatteryLowException() {
        DroneBatteryLowException exception = new DroneBatteryLowException("Drone battery low");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleDroneBatteryLowException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Drone battery low", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleRegisterDroneLimitExceededException() {
        RegisterDroneLimitExceededException exception = new RegisterDroneLimitExceededException("Register drone limit exceeded");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleRegisterDroneLimitExceededException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Register drone limit exceeded", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleDroneAlreadyRegisteredException() {
        DroneAlreadyRegisteredException exception = new DroneAlreadyRegisteredException("Drone already registered");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleDroneAlreadyRegisteredException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Drone already registered", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleDroneCanNotBeLoadedException() {
        DroneCanNotBeLoadedException exception = new DroneCanNotBeLoadedException("Drone cannot be loaded");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleDroneCanNotBeLoadedException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Drone cannot be loaded", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Illegal argument");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleIllegalArgumentException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Illegal argument", Objects.requireNonNull(result.getBody()).message());
    }
    @Test
    void testHandleIllegalStateException() {
        IllegalStateException exception = new IllegalStateException("Illegal state");
        ResponseEntity<ApiResponse<String>> result = globalExceptionHandler.handleIllegalStateException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Illegal state", Objects.requireNonNull(result.getBody()).message());
    }

}

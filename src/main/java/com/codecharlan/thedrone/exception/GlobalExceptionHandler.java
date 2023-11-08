package com.codecharlan.thedrone.exception;

import com.codecharlan.thedrone.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(DroneOverLoadedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleDroneOverLoadedException(DroneOverLoadedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleDroneNotFoundException(DroneNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), NOT_FOUND.value()), NOT_FOUND);
    }
    @ExceptionHandler(DroneBatteryLowException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleDroneBatteryLowException(DroneBatteryLowException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(RegisterDroneLimitExceededException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleRegisterDroneLimitExceededException(RegisterDroneLimitExceededException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(DroneAlreadyRegisteredException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleDroneAlreadyRegisteredException(DroneAlreadyRegisteredException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(DroneCanNotBeLoadedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleDroneCanNotBeLoadedException(DroneCanNotBeLoadedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), e.getMessage(), BAD_REQUEST.value()), BAD_REQUEST);
    }
}

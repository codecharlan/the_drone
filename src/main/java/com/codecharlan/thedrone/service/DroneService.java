package com.codecharlan.thedrone.service;

import com.codecharlan.thedrone.dto.request.DroneRequest;
import com.codecharlan.thedrone.dto.response.ApiResponse;
import com.codecharlan.thedrone.dto.response.DroneBatteryResponse;
import com.codecharlan.thedrone.dto.response.DroneLoadedMedicResponse;
import com.codecharlan.thedrone.dto.response.DroneResponse;
import com.codecharlan.thedrone.entity.Medication;

import java.io.IOException;
import java.util.List;

public interface DroneService {
    ApiResponse<DroneResponse> registerDrone(DroneRequest newDrone);
    ApiResponse<List<DroneResponse>> getAllDrones();
    ApiResponse<DroneResponse> loadMedicationsOntoDrone(String serialNumber, List<Medication> medications) throws IOException;
    ApiResponse<DroneLoadedMedicResponse> getLoadedMedications(String serialNumber);
    ApiResponse<List<DroneResponse>> getAvailableDrones();
    ApiResponse<DroneBatteryResponse> getDroneBatteryLevel(String serialNumber);
}

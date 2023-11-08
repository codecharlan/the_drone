package com.codecharlan.thedrone.dto.response;

public record ApiResponse<T>(String message, T data, int status) {

}

package com.github.felipeschwartz.parkingsystem.service.exceptions;

public class NoActiveContractException extends RuntimeException {
    public NoActiveContractException(String licensePlate) {
        super("No active contract found for license plate: " + licensePlate);
    }
}

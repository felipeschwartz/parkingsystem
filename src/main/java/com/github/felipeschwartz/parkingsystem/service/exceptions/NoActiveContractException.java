package com.github.felipeschwartz.parkingsystem.service.exceptions;

public class NoActiveContractException extends RuntimeException {
    public NoActiveContractException(String licensePlate) {
        super("Nenhum contrato ativo encontrado para a placa: " + licensePlate);
    }
}

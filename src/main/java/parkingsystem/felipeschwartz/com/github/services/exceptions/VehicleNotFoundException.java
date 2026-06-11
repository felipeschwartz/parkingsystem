package parkingsystem.felipeschwartz.com.github.services.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String licensePlate) {
        super("Veículo não encontrado para a placa: " + licensePlate);
    }
}

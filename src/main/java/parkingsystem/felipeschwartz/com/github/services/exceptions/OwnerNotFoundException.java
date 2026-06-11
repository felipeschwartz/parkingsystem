package parkingsystem.felipeschwartz.com.github.services.exceptions;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(Long id) {
        super("Owner not found by ID: " + id);
    }

    public OwnerNotFoundException(String cod) {
        super("Owner not found by CPF/CNPJ: " + cod);
    }


}

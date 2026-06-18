package parkingsystem.felipeschwartz.com.github.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(Long id) {
        super("Owner not found by ID: " + id);
    }

    public ObjectNotFoundException(String cod) {
        super("Owner not found by CPF/CNPJ: " + cod);
    }


}

package es.florida.biblioteca.excepcion;

public class PeticionIncorrectaException extends RuntimeException {
    public PeticionIncorrectaException(String mensaje) {
        super(mensaje);
    }
}

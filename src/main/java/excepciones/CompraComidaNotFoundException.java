package excepciones;

public class CompraComidaNotFoundException extends RuntimeException {
    public CompraComidaNotFoundException(String message) {
        super(message);
    }
}

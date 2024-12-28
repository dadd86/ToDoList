package excepciones;

/**
 * Excepción personalizada para manejar errores relacionados con el SceneManager.
 *
 * <p>Esta clase se utiliza para encapsular errores específicos del manejo de vistas en la
 * aplicación JavaFX, proporcionando mensajes descriptivos y la causa del error (si está disponible).
 * </p>
 */
public class SceneManagerException extends Exception {

    /**
     * Constructor predeterminado.
     */
    public SceneManagerException() {
        super("Error desconocido en SceneManager.");
    }

    /**
     * Constructor que acepta un mensaje descriptivo del error.
     *
     * @param message El mensaje que describe el error.
     */
    public SceneManagerException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta un mensaje descriptivo y la causa del error.
     *
     * @param message El mensaje que describe el error.
     * @param cause   La causa original del error (excepción encadenada).
     */
    public SceneManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor que acepta solo la causa del error.
     *
     * @param cause La causa original del error (excepción encadenada).
     */
    public SceneManagerException(Throwable cause) {
        super("Error en SceneManager causado por otra excepción.", cause);
    }
}

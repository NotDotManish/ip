package chiron;

/**
 * Represents exceptions specific to the Chiron application.
 */
public class ChironException extends Exception {
    /**
     * Constructs a ChironException with the specified validation error message.
     *
     * @param message The error message.
     */
    public ChironException(String message) {
        super(message);
    }
}

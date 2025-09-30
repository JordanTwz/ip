package nami;
/**
 * Domain exception for user-facing validation and parse errors.
 * Messages are written directly to the UI.
 */
public class NamiException extends Exception {
    public NamiException(String message) {
        super(message);
    }
}

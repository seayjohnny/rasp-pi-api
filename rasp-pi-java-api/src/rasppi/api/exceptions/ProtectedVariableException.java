package rasppi.api.exceptions;

/**
 * Created by Dakota on 10/28/2016.
 */
public class ProtectedVariableException extends Exception{
    public ProtectedVariableException() {
    }

    public ProtectedVariableException(String message) {
        super(message);
    }

    public ProtectedVariableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtectedVariableException(Throwable cause) {
        super(cause);
    }
}

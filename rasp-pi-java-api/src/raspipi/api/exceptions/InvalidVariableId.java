package raspipi.api.exceptions;

public class InvalidVariableId  extends Exception {
    public InvalidVariableId(){ super(); }

    public InvalidVariableId(String message){ super(message); }

    public InvalidVariableId(String message, Throwable cause){ super(message, cause);}

    public InvalidVariableId(Throwable cause){ super(cause);}
}

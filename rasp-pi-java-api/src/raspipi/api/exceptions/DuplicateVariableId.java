package raspipi.api.exceptions;

public class DuplicateVariableId  extends Exception {
    public DuplicateVariableId(){ super(); }

    public DuplicateVariableId(String message){ super(message); }

    public DuplicateVariableId(String message, Throwable cause){ super(message, cause);}

    public DuplicateVariableId(Throwable cause){ super(cause);}
}

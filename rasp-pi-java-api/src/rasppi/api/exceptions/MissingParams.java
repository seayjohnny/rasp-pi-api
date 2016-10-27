package rasppi.api.exceptions;

public class MissingParams extends Exception {
    public MissingParams(){ super(); }

    public MissingParams(String message){ super(message); }

    public MissingParams(String message, Throwable cause){ super(message, cause);}

    public MissingParams(Throwable cause){ super(cause);}
}

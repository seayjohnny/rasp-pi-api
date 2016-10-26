package raspipi.api.exceptions;

public class InvalidRequestMethod extends Exception{
    public InvalidRequestMethod(){ super(); }

    public InvalidRequestMethod(String message){ super(message); }

    public InvalidRequestMethod(String message, Throwable cause){ super(message, cause);}

    public InvalidRequestMethod(Throwable cause){ super(cause);}
}

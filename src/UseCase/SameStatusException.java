package UseCase;

/**
 * Will be thrown when the new status is same as current status.
 * s: the String returnedException is thrown.
 */
public class SameStatusException extends Exception{
    public SameStatusException (String s){ super(s);}
}

package UseCase;

/**
 * Will be thrown when the time between two events is too short.
 * s: the String returned when TemplateNotFoundException is thrown.
 */
public class BetweenException extends Exception{
    public BetweenException (String s){ super(s);}
}

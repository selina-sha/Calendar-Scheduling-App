package UseCase;

/**
 * Will be thrown when the duration of event is too short.
 * s: the String returned when TemplateNotFoundException is thrown.
 */
public class DurationException extends Exception{
    public DurationException (String s){ super(s);}
}

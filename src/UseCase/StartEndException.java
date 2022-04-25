package UseCase;

/**
 * Will be thrown when the event end time is prior to the event start time.
 * s: the String returned when TemplateNotFoundException is thrown.
 */
public class StartEndException extends Exception {
    public StartEndException(String s){ super(s);}
}


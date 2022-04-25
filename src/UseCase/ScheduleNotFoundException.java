package UseCase;

/**
 * A class called ScheduleNotFoundException which is a subclass of Exception.
 * It occurs when a schedule with the given schedule id is not found.
 *
 * s: the String returned when ScheduleNotFoundException is thrown.
 *
 * @author Kexin Sha
 * @author Jessica Wang
 * @author Hilda Wang
 */
public class ScheduleNotFoundException extends Exception{
    public ScheduleNotFoundException(String s){
        super(s);}
}
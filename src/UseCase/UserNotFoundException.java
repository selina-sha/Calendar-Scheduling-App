package UseCase;

/**
 * Will be thrown when the user is not found
 * s: the String returned when TemplateNotFoundException is thrown.
 */
public class UserNotFoundException extends Exception{
    public UserNotFoundException (String s) {
        super(s); }
}

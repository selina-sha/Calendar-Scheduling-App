package UseCase;

/**
 * A class called TemplateNotFoundException which is a subclass of Exception.
 * It occurs when a Template with the given template id is not found.
 *
 * s: the String returned when TemplateNotFoundException is thrown.
 *
 * @author Christine
 * @author Chuanrun Zhang
 * @author Siqing Xu
 */
public class TemplateNotFoundException extends Exception{
        public TemplateNotFoundException(String s){ super(s);}
    }

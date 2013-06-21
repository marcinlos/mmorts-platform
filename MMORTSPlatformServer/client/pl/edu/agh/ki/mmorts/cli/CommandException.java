package pl.edu.agh.ki.mmorts.cli;

/**
 * Base class for all the command-related exceptions except for those related
 * to parsing.
 * 
 * @author los
 */
public class CommandException extends Exception {
    
    public CommandException() { 
        // empty
    }
    
    public CommandException(String message) {
        super(message);
    }
    
    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

}

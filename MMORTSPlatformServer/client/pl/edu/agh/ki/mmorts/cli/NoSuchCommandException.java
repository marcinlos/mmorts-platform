package pl.edu.agh.ki.mmorts.cli;

/**
 * Thrown to indicate the lack of command with a given name
 * 
 * @author los
 */
public class NoSuchCommandException extends CommandException {
    
    private String command;

    public NoSuchCommandException(String command) {
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }

}

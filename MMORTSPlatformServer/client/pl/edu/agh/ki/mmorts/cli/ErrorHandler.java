package pl.edu.agh.ki.mmorts.cli;

public interface ErrorHandler {
    
    boolean onError(CommandException e) throws CommandException;

}

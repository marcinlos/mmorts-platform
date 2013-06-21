package pl.edu.agh.ki.mmorts.cli;


/**
 * Interface of a generic line command interpreter.
 * 
 * @author los
 */
public interface Interpreter {

    /**
     * Interprets the {@code link} line.
     * 
     * @param line
     *            line to be interpreted
     * @return {@code false} if the interpreter session should finish.
     *         {@code true} otherwise
     * @throws CommandException
     *             when the command invocation fails
     */
    boolean interpret(String line) throws CommandException;

}

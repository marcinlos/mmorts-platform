package pl.edu.agh.ki.mmorts.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

/**
 * Class for creating command line interfaces.
 */
public class InputSource {

    /** Input source */
    protected final BufferedReader input;

    /** Output */
    protected final PrintStream output = System.out;

    /** Used to interpret read lines */
    private Interpreter interpreter;

    /** Invoked upon ecxeption while interpreting the line */
    private ErrorHandler errorHandler = new DefaultErrorHandler();

    /**
     * Creates a command line interface using standard input.
     */
    public InputSource() throws IOException {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    /** Whether or not print a prompt after each command */
    private boolean promptEnabled = false;

    /** Prompt to print */
    private String prompt = "> ";

    /**
     * Creates a command line interface using given {@code Reader} instance as
     * an input source.
     */
    public InputSource(Reader reader) {
        // Check whether the reader already is buffered
        if (reader instanceof BufferedReader) {
            input = (BufferedReader) reader;
        } else {
            input = new BufferedReader(reader);
        }
    }

    /**
     * Creates a command line interface using given {@code Stream} instance as
     * an input source.
     */
    public InputSource(InputStream stream) {
        this(new InputStreamReader(stream));
    }

    /**
     * Sets the internal command interpreter.
     * 
     * @param interpreter
     *            {@linkplain Interpreter} instance to be used to interpret
     *            input.
     */
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * Sets a new prompt string. Returns previously used value.
     * 
     * @param prompt
     *            New prompt string to use
     * @return Old prompt string
     */
    public String setPrompt(String prompt) {
        String old = this.prompt;
        this.prompt = prompt;
        return old;
    }

    /**
     * Sets the error handler used to process exceptions while interpreting
     * input. Specifically, {@code handler}'s
     * {@linkplain ErrorHandler#onError(CommandException)} shall be invoked in
     * case the {@linkplain Interpreter#interpret(String)} invocation throws a
     * {@linkplain CommandException} with this exception as an argument.
     * 
     * <p>
     * If no error handler is explicitly provided,
     * {@linkplain DefaultErrorHandler} is used.
     * 
     * @param handler
     *            {@linkplain ErrorHandler} instance to be used as
     */
    public void setErrorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    /**
     * Input-consuming and interpreting loop.
     * 
     * @throws IOException
     *             if reading the standard input fails
     * @throws CommandException
     *             if there is a problem during an attempt to invoke the command
     */
    public void run() throws IOException, CommandException {
        String line;
        while ((line = input.readLine()) != null) {
            try {
                if (!interpret(line)) {
                    break;
                }
            } catch (CommandException e) {
                if (errorHandler != null) {
                    if (!errorHandler.onError(e)) {
                        break;
                    }
                } else {
                    // Rethrow if error handler is missing
                    throw e;
                }
            } finally {
                if (promptEnabled) {
                    printPrompt();
                }
            }
        }
        System.out.println("\r");
    }

    /**
     * Prints the prompt to the standard output and flushes
     */
    private void printPrompt() {
        output.print(prompt);
        output.flush();
    }
    
    public void enablePrompt() {
        promptEnabled = true;
    }
    
    public void disablePrompt() {
        promptEnabled = false;
    }

    /**
     * Actual interpreting function, gets its input from {@link #run()} and
     * processes it.
     * 
     * @param line
     *            whole line from the terminal
     * @return {@code true} if the server should continue running, {@code false}
     *         otherwise.
     * @throws ParseException
     *             if the input line cannot be correctly parsed
     * @throws NoSuchCommandException
     *             if the input line cannot be recognized as a command
     *             invocation
     * @throws CommandExecutionException
     *             wrapping exceptions thrown by commands
     */
    protected boolean interpret(String line) throws CommandException {
        if (interpreter != null) {
            return interpreter.interpret(line);
        } else {
            throw new IllegalStateException("No interpreter");
        }
    }
}
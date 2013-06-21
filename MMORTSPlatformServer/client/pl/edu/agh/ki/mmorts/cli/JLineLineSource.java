package pl.edu.agh.ki.mmorts.cli;

import java.io.IOException;

import jline.ConsoleReader;

/**
 * Line source based on JLine - terminal handling library, kind of like GNU
 * <tt>readline</tt>. Primary gain is automatic handling of line editing and
 * history.
 * 
 * @author los
 */
public class JLineLineSource implements LineSource {

    private ConsoleReader console;

    /**
     * Creates a new line source using the default {@linkplain ConsoleReader}
     * 
     * @throws IOException
     *             If there is an error during console reader creation
     */
    public JLineLineSource() throws IOException {
        this(new ConsoleReader());
    }

    /**
     * Creates new line source using passed {@linkplain ConsoleReader} object.
     * 
     * @param console
     *            Console object to use as an input source
     */
    public JLineLineSource(ConsoleReader console) {
        this.console = console;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLine() throws IOException {
        return console.readLine("> ");
    }

}

package pl.edu.agh.ki.mmorts.cli;

import java.io.IOException;

/**
 * General line source - file, console etc. String iterator could be used
 * instead.
 */
public interface LineSource {

    /**
     * @return New line to interpret of {@code null} if there is no input.
     */
    String getLine() throws IOException;
}

package pl.edu.agh.ki.mmorts.cli;

import java.io.IOException;

import jline.ConsoleReader;

public class JLineLineSource implements LineSource {

    private ConsoleReader console;

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

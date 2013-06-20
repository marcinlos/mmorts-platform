package pl.edu.agh.ki.mmorts.cli;

import java.util.Scanner;

/**
 * Implementation wrapping scanner.
 * 
 * @author los
 */
public class ScannerLineSource implements LineSource {

    private Scanner scanner;

    /**
     * Creates a {@code ScannerLineSource} using given scanner as the input
     * source.
     * 
     * @param scanner
     *            {@code Scanner} used as the input source
     */
    public ScannerLineSource(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLine() {
        if (scanner.hasNextLine()) {
        return scanner.nextLine();
        } else {
            return null;
        }
    }

}

package pl.edu.agh.ki.mmorts.testclient;

import jline.ConsoleReader;
import pl.edu.agh.ki.mmorts.cli.Controller;
import pl.edu.agh.ki.mmorts.cli.JLineLineSource;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.util.reflection.Methods;

public class Main {

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client(args);
            ConsoleReader reader = new ConsoleReader();
            Controller input = new Controller(new JLineLineSource(reader));
            input.setInterpreter(client);
            input.run();
        } catch (Exception e) {
            System.err.println("Exception: ");
            e.printStackTrace(System.err);
        } finally {
            if (client != null) {
                Methods.callAnnotated(OnShutdown.class, client);
            }
        }
    }

}

package pl.edu.agh.ki.mmorts.testclient;

import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.util.reflection.Methods;

public class Main {

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client(args);
            client.repl();
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

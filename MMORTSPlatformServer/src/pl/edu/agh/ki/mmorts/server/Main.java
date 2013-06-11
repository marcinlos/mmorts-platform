package pl.edu.agh.ki.mmorts.server;

import pl.edu.agh.ki.mmorts.server.core.Init;

public class Main {

    private Main() {
        // non-ins
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Init init = new Init(args);
    }

}

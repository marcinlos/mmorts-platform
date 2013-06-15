package pl.edu.agh.ki.mmorts.testclient;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import pl.edu.agh.ki.mmorts.DispatcherPrx;
import pl.edu.agh.ki.mmorts.DispatcherPrxHelper;
import pl.edu.agh.ki.mmorts.Response;
import pl.edu.agh.ki.mmorts.common.ice.Translator;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import Ice.ObjectPrx;

public class Client extends Ice.Application {

    private DispatcherPrx dispatcher;

    private static final String PROMPT = "> ";

    @Override
    public int run(String[] args) {
        String str = communicator().getProperties().getProperty("MMORTSServer.Proxy");
        ObjectPrx obj = communicator().stringToProxy(str);
        System.out.print("Obtaining server reference...");
        System.out.flush();
        try {
            dispatcher = DispatcherPrxHelper.checkedCast(obj);
            if (dispatcher == null) {
                System.err.println("Failed to obtain dispatcher reference");
                System.exit(1);
            }
            System.out.println("done");
        } catch (Ice.ConnectionRefusedException e) {
            System.out.println("\nConnection refused, is server running?");
            return 1;
        }
        try {
            repl();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } finally {
            communicator().destroy();
        }
        return 0;
    }

    private void repl() throws IOException {
        System.out.println("MMORTS Platform");
        System.out.println("Test client");
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            System.out.print(PROMPT);
            System.out.flush();
            String line = input.nextLine();
            if (!line.isEmpty()) {
                interpret(new Scanner(line));
            }
        }
    }

    private void interpret(Scanner scanner) {
        String cmd = scanner.next();
        if (cmd.equals("msg")) {
            parseMessage(scanner);
        }
    }

    private void parseMessage(Scanner scanner) {
        try {
            String address = scanner.next();
            String m = scanner.next();
            Mode mode;
            if (m.equals("U")) {
                mode = Mode.UNICAST;
            } else {
                mode = Mode.MULTICAST;
            }
            String type = scanner.next();
            Message msg = new Message(66, "ja", address, mode, type, "Siema");
            Response resp = dispatcher.deliver(Translator.iceify(msg));
            System.out.println("Response:");
            for (pl.edu.agh.ki.mmorts.Message iceMsg: resp.messages) {
                Message message = Translator.deiceify(iceMsg);
                System.out.println("* + " + message);
            }

        } catch (NoSuchElementException e) {
            System.err.println("usage: msg address (U|M) type");
        }
    }

}

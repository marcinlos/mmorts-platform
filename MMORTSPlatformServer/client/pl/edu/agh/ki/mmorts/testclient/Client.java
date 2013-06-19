package pl.edu.agh.ki.mmorts.testclient;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import pl.edu.agh.ki.mmorts.DispatcherPrx;
import pl.edu.agh.ki.mmorts.DispatcherPrxHelper;
import pl.edu.agh.ki.mmorts.Response;
import pl.edu.agh.ki.mmorts.client.communication.MessageOutputChannel;
import pl.edu.agh.ki.mmorts.client.communication.ice.IceOutputChannel;
import pl.edu.agh.ki.mmorts.common.ice.Translator;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import Ice.ObjectPrx;

/**
 * Test client application.
 * 
 * @author los
 */
public class Client {

    private static final String PROMPT = "> ";
    
    private final MessageOutputChannel channel;

    public Client(String[] args) {
        channel = new IceOutputChannel(args);
    }

    void repl() throws IOException {
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
            List<Message> response = channel.send(msg);
            System.out.println("Response:");
            for (Message message: response) {
                System.out.println("* + " + message);
            }
        } catch (NoSuchElementException e) {
            System.err.println("usage: msg address (U|M) type");
        }
    }

}

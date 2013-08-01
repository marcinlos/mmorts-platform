package pl.edu.agh.ki.mmorts.testclient;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import pl.edu.agh.ki.mmorts.cli.CommandException;
import pl.edu.agh.ki.mmorts.cli.Interpreter;
import pl.edu.agh.ki.mmorts.client.communication.MessageOutputChannel;
import pl.edu.agh.ki.mmorts.client.communication.ResponseCallback;
import pl.edu.agh.ki.mmorts.client.communication.ice.IceOutputChannel;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.MessagePack;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.modules.basic.map.commons.RequestType;
import pl.edu.agh.ki.mmorts.server.util.reflection.Methods;
import protocol.mapModule.DetailedMessage;
import protocol.mapModule.SimpleMessage;

/**
 * Test client application.
 * 
 * @author los
 */
public class Client implements Interpreter {

    /** Message channel */
    private final MessageOutputChannel channel;

    public Client(String[] args) {
        channel = new IceOutputChannel(args);
    }

    /**
     * Closes the channel
     */
    @OnShutdown
    private void shutdown() {
        Methods.callAnnotated(OnShutdown.class, channel);
    }

    /**
     * Parses the message in the format <tt>address (U|M|) [Map C|F|P|R [player] [[row] [col]]{only for C,R,P}] type</tt> from the
     * given scanner.
     * 
     * @param scanner
     *            Scanner to take input from
     * @return Parsed message
     */
    private Message parseMessage(Scanner scanner) {
        String address = scanner.next();
        String m = scanner.next();
        Mode mode = null;
        if (m.equals("U")) {
            mode = Mode.UNICAST;
        } else if(m.equals("M")) {
            mode = Mode.MULTICAST;
        }
        String typeOrMap = scanner.next();
        if(typeOrMap.equals("Map")){
        	return parseMapMessage(scanner, address, mode);
        }
        String type = typeOrMap;
        Message msg = new Message(66, "source", address, mode, type, "Content");
        return msg;
    }

    private Message parseMapMessage(Scanner scanner, String address, Mode mode) {
		String messgType = scanner.next();
		String player = scanner.next();
		Message toSendMessage = null;
		SimpleMessage messg = null;
		String reqString = null;
		if(messgType.equals("C")){
			int row = scanner.nextInt();
			int col = scanner.nextInt();
			messg = new DetailedMessage(player, row, col);
			reqString = RequestType.CHECK.getMessg();
		}else if(messgType.equals("F")){
			messg = new SimpleMessage(player);
			reqString = RequestType.FULL.getMessg();
		}else if(messgType.equals("R")){
			int row = scanner.nextInt();
			int col = scanner.nextInt();
			messg = new DetailedMessage(player, row, col);
			reqString = RequestType.REL_AT.getMessg();
		}else if(messgType.equals("P")){
			int row = scanner.nextInt();
			int col = scanner.nextInt();
			messg = new DetailedMessage(player, row, col);
			reqString = RequestType.PUT_ON.getMessg();
		}
		System.out.println("message creating");
		toSendMessage = new Message(2, "test", address, mode, reqString , messg);
		return toSendMessage;
	}

	/**
     * Sends a single message as read from the input scanner.
     * 
     * @param scanner
     */
    private void sendSingleMessage(Scanner scanner) {
        try {
            Message msg = parseMessage(scanner);
            MessagePack response = channel.send(msg);
            System.out.println("Response:");
            System.out.println("version: " + response.version);
            for (Message message : response.messages) {
                System.out.println("* + " + message);
                System.out.println("===Messg===");
                System.out.println(message.get(Object.class));
            }
        } catch (NoSuchElementException e) {
        	e.printStackTrace();
            System.err.println("usage: msg address (U|M) type");
        }
    }

    /**
     * Helper for testing absolute raw throughput of the server.
     */
    private class StressTester implements Runnable {

        /** How often are the bursts sent */
        static final int BURST_COUNT = 20;

        Message message;
        AtomicInteger failures = new AtomicInteger(0);

        int totalCount;
        int perSec;
        int burstCount;

        public StressTester(Message message, int secs, int perSec) {
            this.message = message;
            this.totalCount = secs * perSec;
            this.perSec = perSec;
            this.burstCount = perSec <= BURST_COUNT ? perSec : BURST_COUNT;
        }

        @Override
        public void run() {
            final int millis = 1000 / burstCount;
            final int burstSize = perSec / burstCount;
            final CountDownLatch latch = new CountDownLatch(totalCount);
            try {
                final long before = System.currentTimeMillis();
                long burstStart = before;
                for (int i = 0; i < totalCount; ++i) {
                    channel.sendAsync(message, new ResponseCallback() {
                        @Override
                        public void responded(MessagePack messages) {
                            latch.countDown();
                        }

                        @Override
                        public void failed(Exception e) {
                            failures.incrementAndGet();
                            latch.countDown();
                        }
                    });
                    // Send burst
                    if (i % burstSize == 0) {
                        updateProgressBar(i);
                        long now = System.currentTimeMillis();
                        long elapsed = now - burstStart;
                        TimeUnit.MILLISECONDS.sleep(millis - elapsed);
                        burstStart = System.currentTimeMillis();
                    }
                }
                if (!latch.await(5, TimeUnit.SECONDS)) {
                    System.err.println("Timeout");
                }
                // Last message
                long now = System.currentTimeMillis();
                long time = now - before;
                System.out.println("\nOK: " + time + "ms");
                double actualCPS = 1000 * totalCount / (double) time;
                System.out.format("%.1f calls/s\n", actualCPS);
                int lost = failures.get() + (int) latch.getCount();
                int ok = totalCount - lost;
                double lossPerc = 100 * (double) lost / totalCount;
                System.out.format("%d/%d (%.2f%% loss)\n", ok, totalCount,
                        lossPerc);
            } catch (InterruptedException e) {
                System.out.println("Interrupted...");
                Thread.currentThread().interrupt();
            }
        }

        private void updateProgressBar(int i) {
            StringBuilder sb = new StringBuilder("\r|");
            final int size = 40;
            final int filled = (int) (size * ((double) i / totalCount));
            for (int j = 0; j < size - 1; ++j) {
                sb.append(j < filled ? '*' : ' ');
            }
            sb.append("|");
            System.out.print(sb.toString());
            System.out.flush();
        }

    }

    private void floodMessage(Scanner scanner) {
        try {
            final int secs = scanner.nextInt();
            final int perSec = scanner.nextInt();

            Message msg = parseMessage(scanner);
            new StressTester(msg, secs, perSec).run();
        } catch (NoSuchElementException e) {
            System.err.println("usage: flood sec per_sec address (U|M) type");
        }
    }

    @Override
    public boolean interpret(String line) throws CommandException {
        Scanner scanner = new Scanner(line);
        String first = scanner.next();
        if (first.equals("msg")) {
            sendSingleMessage(scanner);
        } else if (first.equals("flood")) {
            floodMessage(scanner);
        }
        return true;
    }

}

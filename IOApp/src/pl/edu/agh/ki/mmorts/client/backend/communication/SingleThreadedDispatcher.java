package pl.edu.agh.ki.mmorts.client.backend.communication;

import android.util.Log;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.MessagePack;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionExecutor;


public class SingleThreadedDispatcher extends AbstractDispatcher {
	
	private static final String ID =SingleThreadedDispatcher.class.getName();
	
	private TransactionExecutor executor = new TransactionExecutor();

	@Override
	public void receive(MessagePack message) {
		System.out.println("Message: ");
		System.out.println("version: " + message.version);
		Log.d(ID, String.format("Got message %s", message));
		for (Message msg: message.messages) {
			dispatchMessage(msg);
		}
	}

	@Override
	protected TransactionExecutor executor() {
		return executor;
	}

	@Override
	protected void onShutdown() {
		// TODO Auto-generated method stub

	}
	
	public MessagePack sendWithResponse(Message message){
		return channel().send(message);
	}
	
	//Hack!
	@Override
	public void send(Message message) {
		MessagePack mPack = channel().send(message);
		receive(mPack);
	}

}

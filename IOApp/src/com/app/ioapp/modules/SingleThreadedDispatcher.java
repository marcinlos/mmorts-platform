package com.app.ioapp.modules;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.MessagePack;

public class SingleThreadedDispatcher extends AbstractDispatcher {
	
	private TransactionExecutor executor = new TransactionExecutor();

	@Override
	public void receive(MessagePack message) {
		System.out.println("Message: ");
		System.out.println("version: " + message.version);
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

}

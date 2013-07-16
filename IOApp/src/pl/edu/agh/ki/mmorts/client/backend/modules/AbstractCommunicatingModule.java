package pl.edu.agh.ki.mmorts.client.backend.modules;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;




/**
 * Implementation of {@code CommunicatingModule}
 *
 */
public abstract class AbstractCommunicatingModule implements CommunicatingModule{
	 
	@Override
	 public void started() {
		 
	 }
	 @Override
	 public void receive(Message message, TransactionContext ctx) {
		 
	 }
	 @Override
	 public void shutdown() {
		 
	 }

}

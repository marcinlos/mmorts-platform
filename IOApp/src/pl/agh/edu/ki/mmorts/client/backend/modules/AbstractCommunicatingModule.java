package pl.agh.edu.ki.mmorts.client.backend.modules;


import pl.edu.agh.ki.mmorts.common.message.Message;


/**
 * Implementation of {@code CommunicatingModule}
 *
 */
public abstract class AbstractCommunicatingModule implements CommunicatingModule{
	 
	@Override
	 public void started() {
		 
	 }
	 @Override
	 public void receive(Message message, Context ctx) {
		 
	 }
	 @Override
	 public void shutdown() {
		 
	 }

}

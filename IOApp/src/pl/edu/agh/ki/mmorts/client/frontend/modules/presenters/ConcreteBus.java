package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;

public class ConcreteBus implements Bus {
	
	private List<BusListener> l;

	@Override
	public void sendMessage(PresentersMessage m) {
		for(BusListener bl : l){
			bl.gotMessage(m);
		}

	}

	@Override
	public void register(BusListener bl) {
		if(l == null){
			l = new ArrayList<BusListener>();
		}
		l.add(bl);
	}

}

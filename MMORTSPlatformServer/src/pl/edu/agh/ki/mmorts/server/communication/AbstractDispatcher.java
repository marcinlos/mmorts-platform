package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.modules.Module;



public class AbstractDispatcher implements Gateway, Dispatcher {

    public AbstractDispatcher() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void registerModules(Module... modules) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerModules(Iterable<? extends Module> modules) {
        System.out.println("Panie Rakoczy,prosze sie ogarnac");
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerUnicastReceiver(Module module, String category) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendTo(Message message, String address) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void send(Message mesage, String category) {
        // TODO Auto-generated method stub
        
    }

}

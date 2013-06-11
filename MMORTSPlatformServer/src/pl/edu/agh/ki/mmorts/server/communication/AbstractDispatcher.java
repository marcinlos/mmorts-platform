package pl.edu.agh.ki.mmorts.server.communication;

import java.util.Arrays;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.modules.Module;



public abstract class AbstractDispatcher implements Gateway, Dispatcher {

    public AbstractDispatcher() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void registerModules(Module... modules) {
        registerModules(Arrays.asList(modules));
    }

}

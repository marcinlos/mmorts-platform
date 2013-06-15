package pl.edu.agh.ki.mmorts.server.modules;


import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Cont;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Control;
import pl.edu.agh.ki.mmorts.server.modules.dsl.DSL;

import com.google.inject.Inject;


public abstract class ModuleBase implements Module {
    
    @Inject(optional = true)
    private Gateway gateway;
    
    @Inject(optional = true)
    private ModuleDescriptor descriptor;
    
    private Control control;

    @OnInit
    void initControl() {
        control = new Control() {
            @Override
            public void continueWith(final Cont c) {
                gateway.later(new Continuation() {
                    @Override
                    public void failure(Throwable e, Context context) {
                        // empty
                    }
                    
                    @Override
                    public void execute(Context context) {
                        c.execute(control);
                    }
                });
            }
        };
    }
    
    protected Gateway gateway() {
        return gateway;
    }
    
    protected ModuleDescriptor descriptor() {
        return descriptor;
    }
    
    protected Control control() {
        return control;
    }
    
    protected void call(Cont cont) {
        DSL.with(control, cont);
    }
    
    protected void sendResponse(Message response) {
        gateway.sendResponse(response);
    }
    
    protected void send(Message message) {
        gateway.send(message);
    }
    
    protected void sendDelayed(Message message) {
        gateway.sendDelayed(message);
    }
    
}

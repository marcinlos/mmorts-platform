package pl.edu.agh.ki.mmorts.server.modules;


import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Cont;
import pl.edu.agh.ki.mmorts.server.modules.dsl.Control;

import com.google.inject.Inject;


public abstract class ModuleBase implements Module {
    
    @Inject(optional = true)
    protected Gateway gateway;
    
    protected Control control;

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


}

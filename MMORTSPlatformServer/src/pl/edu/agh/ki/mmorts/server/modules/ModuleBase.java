package pl.edu.agh.ki.mmorts.server.modules;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Messages;
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

    /**
     * Initializes the control
     */
    @OnInit
    private void initModule() {
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

    /**
     * @return Gateway for the communication and control flow
     */
    protected Gateway gateway() {
        return gateway;
    }

    /**
     * @return Module descriptor
     */
    protected ModuleDescriptor descriptor() {
        return descriptor;
    }

    /**
     * @return Name of the module
     */
    protected String name() {
        return descriptor().name;
    }

    /**
     * @return Arbitrary unicast address associated with the module
     */
    protected String anyAddress() {
        Collection<String> uni = descriptor().unicast;
        if (!uni.isEmpty()) {
            return uni.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * @return Control flow interface
     */
    protected Control control() {
        return control;
    }

    /**
     * Executes (i.e. pushes on the transaction execution stack) given
     * {@linkplain Cont}
     * 
     * @param cont
     *            {@code Cont} to execute later
     * 
     */
    protected void call(Cont cont) {
        DSL.with(control, cont);
    }

    /**
     * Executes (i.e. pushes on the transaction execution stack) given
     * {@linkplain Continuation}
     * 
     * @param cont
     *            {@code Continuation} to execute later
     */
    protected void later(Continuation cont) {
        gateway.later(cont);
    }

    /**
     * Forwards to {@link Gateway#sendResponse}, convenience method.
     * 
     * @param response
     *            Response to send
     */
    protected void sendResponse(Message response) {
        gateway.sendResponse(response);
    }
    
    //protected void sendResponse()

    /**
     * Forwards to {@link Gateway#send}, convenience method.
     * 
     * @param message
     *            Message to send
     */
    protected void send(Message message) {
        gateway.send(message);
    }

    /**
     * Forwards to {@link Gateway#sendDelayed}, convenience method.
     * 
     * @param message
     *            Message to send
     */
    protected void sendDelayed(Message message) {
        gateway.sendDelayed(message);
    }

    /**
     * Convenience method, sends a delayed multicast message with any of the
     * module's unicast addresses as a source, and with a given request string.
     * Carried data is {@code null}.
     * 
     * @param group
     *            Name of the multicast group to which the notification is to be
     *            delivered
     * @param request
     *            Request string
     * @see #anyAddress()
     */
    protected void sendNotification(String group, String request) {
        sendDelayed(Messages.multicast(0, anyAddress(), group, request, null));
    }

    /**
     * Convenience method, sends a delayed multicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param group
     *            Name of the multicast group to which the notification is to be
     *            delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the notification
     */
    protected void sendNotification(String group, String request, Object data) {
        sendDelayed(Messages.multicast(0, anyAddress(), group, request, data));
    }

}

package com.app.ioapp.modules;

import java.util.Collection;


import pl.edu.agh.ki.mmorts.client.core.transaction.Transaction;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionProvider;
import pl.edu.agh.ki.mmorts.client.modules.annotations.impl.CallDispatcher;
import pl.edu.agh.ki.mmorts.client.modules.annotations.impl.TrivialMapperFactory;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Messages;

import android.util.Log;

import com.app.ioapp.communication.Gateway;
import com.app.ioapp.config.Config;
import com.google.inject.Inject;

/**
 * Abstract base class for modules, containing many neat features to facilitate
 * module developement.
 * 
 * @author los
 */
public abstract class ModuleBase implements Module {

    /** Global immutable configuration */
    @Inject(optional = true)
    private Config config;

    /** Communication gateway and flow control */
    @Inject(optional = true)
    private Gateway gateway;
    
    /** Service locator */
    @Inject(optional = true)
    private ServiceLocator services;

    /** Module-specific configuration */
    @Inject(optional = true)
    private ModuleDescriptor descriptor;

    /** Transaction provider */
    @Inject(optional = true)
    private TransactionProvider txProvider;


    /** Call dispatcher */
    private CallDispatcher callDispatcher = new CallDispatcher(getClass(),
            new TrivialMapperFactory());
    
    private static final String ID = "ModuleBase";


    /**
     * Dispatches the message using annotation-based dispatcher.
     * 
     * @param message
     *            Message to dispatch
     * @param context
     *            Associated context
     */
    protected void dispatchMessage(Message message, Context context) {
        callDispatcher.handle(this, message, context);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Forwards to {@linkplain #dispatchMessage(Message, Context)}. Override it if
     * you need a different behaviour.
     */
    @Override
    public void receive(Message message, Context context) {
        dispatchMessage(message, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        Log.d(ID, "Module " + name() + " init()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        Log.d(ID, "Module " + name() + " started()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        Log.d(ID, "Module " + name() + " shutdown()");
    }

    /**
     * @return Global configuration
     */
    protected final Config config() {
        return config;
    }

    /**
     * @return Gateway for the communication and control flow
     */
    protected final Gateway gateway() {
        return gateway;
    }
    
    /**
     * @return Service locator
     */
    protected final ServiceLocator services() {
        return services;
    }

    /**
     * @return Module descriptor
     */
    protected final ModuleDescriptor descriptor() {
        return descriptor;
    }

    /**
     * @return Current transaction associated with this thread
     */
    protected final Transaction transaction() {
        return txProvider.getCurrent();
    }

    /**
     * @return Name of the module
     */
    protected final String name() {
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
     * Constructs a response for the {@code message} with a new request string
     * and sends it through the gateway.
     * 
     * @param message
     *            Message to which we respond
     * @param newRequest
     *            Request string of the response
     * @see Message#response(String)
     */
    protected void respond(Message message, String newRequest) {
        gateway.send(message.response(newRequest));
    }

    /**
     * Constructs a response for the {@code message} with a new source and
     * request string and sends it through the gateway.
     * 
     * @param message
     *            Message to which we respond
     * @param src
     *            Source address of the response
     * @param newRequest
     *            Request string of the response
     */
    protected void respond(Message message, String src, String newRequest) {
        gateway.send(message.response(src, newRequest, null));
    }

    /**
     * Constructs a response for the {@code message} with a given request string
     * and data and sends it through the gateway.
     * 
     * @param message
     *            Message to which we respond
     * @param newRequest
     *            Request string of the response
     * @param data
     *            Data carried in the response
     * @see Message#response(String, Object)
     */
    protected void respond(Message message, String newRequest, Object data) {
        gateway.send(message.response(newRequest, data));
    }

    /**
     * Constructs a response for the {@code message} with a specified source,
     * request string and data and sends it through the gateway.
     * 
     * @param message
     *            Message to which we respond
     * @param src
     *            Source address of the response
     * @param newRequest
     *            Request string of the response
     * @param data
     *            Data carried in the response
     * @see Message#response(String, String, Object)
     */
    protected void respond(Message message, String src, String newRequest,
            Object data) {
        gateway.send(message.response(src, newRequest, data));
    }


    /**
     * Convenience method, sends an immediate unicast message with any of the
     * module's unicast addresses as a source, and with a given request string.
     * Carried data is {@code null}.
     * 
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     * @see #anyAddress()
     */
    protected void send(String address, String request) {
        send(Messages.unicast(0, anyAddress(), address, request, null));
    }

    /**
     * Convenience method, sends an immediate unicast message with specified
     * unicast addresses as a source, and with a given request string. Carried
     * data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     */
    protected void send(String src, String address, String request) {
        send(Messages.unicast(0, src, address, request, null));
    }

    /**
     * Convenience method, sends an immediate unicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void send(String address, String request, Object data) {
        send(Messages.unicast(0, anyAddress(), address, request, data));
    }

    /**
     * Convenience method, sends an immediate unicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void send(String src, String address, String request, Object data) {
        send(Messages.unicast(0, src, address, request, data));
    }

    /**
     * Convenience method, sends a delayed unicast message with any of the
     * module's unicast addresses as a source, and with a given request string.
     * Carried data is {@code null}.
     * 
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     * @see #anyAddress()
     */
    protected void sendDelayed(String address, String request) {
        sendDelayed(Messages.unicast(0, anyAddress(), address, request, null));
    }

    /**
     * Convenience method, sends a delayed unicast message with specified
     * unicast addresses as a source, and with a given request string. Carried
     * data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     */
    protected void sendDelayed(String src, String address, String request) {
        sendDelayed(Messages.unicast(0, src, address, request, null));
    }

    /**
     * Convenience method, sends a delayed unicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void sendDelayed(String address, String request, Object data) {
        sendDelayed(Messages.unicast(0, anyAddress(), address, request, data));
    }

    /**
     * Convenience method, sends a delayed unicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Source address of the message
     * @param address
     *            Unicast address to which the message is to be delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void sendDelayed(String src, String address, String request,
            Object data) {
        sendDelayed(Messages.unicast(0, src, address, request, data));
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
     * Convenience method, sends a delayed multicast message with specified
     * unicast addresses as a source, and with a given request string. Carried
     * data is {@code null}.
     * 
     * @param src
     *            Sourc address of the notification
     * 
     * @param group
     *            Name of the multicast group to which the notification is to be
     *            delivered
     * @param request
     *            Request string
     */
    protected void sendNotification(String src, String group, String request) {
        sendDelayed(Messages.multicast(0, src, group, request, null));
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

    /**
     * Convenience method, sends a delayed multicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Sourc address of the notification
     * @param group
     *            Name of the multicast group to which the notification is to be
     *            delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the notification
     */
    protected void sendNotification(String src, String group, String request,
            Object data) {
        sendDelayed(Messages.multicast(0, src, group, request, data));
    }

    /**
     * Convenience method, sends an immediate multicast message with any of the
     * module's unicast addresses as a source, and with a given request string.
     * Carried data is {@code null}.
     * 
     * @param group
     *            Name of the multicast group to which the message is to be
     *            delivered
     * @param request
     *            Request string
     * @see #anyAddress()
     */
    protected void sendMulticast(String group, String request) {
        send(Messages.multicast(0, anyAddress(), group, request, null));
    }

    /**
     * Convenience method, sends an immediate multicast message with specified
     * unicast addresses as a source, and with a given request string. Carried
     * data is {@code null}.
     * 
     * @param src
     *            Sourc address of the notification
     * 
     * @param group
     *            Name of the multicast group to which the message is to be
     *            delivered
     * @param request
     *            Request string
     */
    protected void sendMulticast(String src, String group, String request) {
        send(Messages.multicast(0, src, group, request, null));
    }

    /**
     * Convenience method, sends an immediate multicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param group
     *            Name of the multicast group to which the message is to be
     *            delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void sendMulticast(String group, String request, Object data) {
        send(Messages.multicast(0, anyAddress(), group, request, data));
    }

    /**
     * Convenience method, sends an immediate multicast message with any of the
     * module's unicast addresses as a source, given request string and carrying
     * {@code data}. Carried data is {@code null}.
     * 
     * @param src
     *            Sourc address of the notification
     * @param group
     *            Name of the multicast group to which the message is to be
     *            delivered
     * @param request
     *            Request string
     * @param data
     *            Data carried with the message
     */
    protected void sendMulticast(String src, String group, String request,
            Object data) {
        send(Messages.multicast(0, src, group, request, data));
    }

}

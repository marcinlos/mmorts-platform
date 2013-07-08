package com.app.ioapp.modules;


import javax.inject.Inject;

import pl.edu.agh.ki.mmorts.common.message.Message;

import com.google.inject.name.Named;


/**
 * Interface of the module, basic unit of functionality. Module represents an
 * arbitrary enhancement and customisation of the server in the area of an
 * interaction with the client.
 * 
 * <p>
 * Modules are message-based request handlers, reacting on messages delivered by
 * the dispatcher. Module can be registered with at most one unicast address,
 * and an arbitrary number of multicast addresses. Unicast and multicast address
 * spaces are disjoint and independant. Messages sent to the module (on its'
 * unicast address or one of its' multicast addresses) are passed to the
 * module's {@linkplain #receive} method, together with the request context.
 * 
 * <p>
 * Requests are processed in a transactional manner. At the beginning of the
 * request, transaction is initiated. To cause rollback, module can at any point
 * throw {@linkplain ModuleLogicException} if module logic precludes commiting.
 * Any other exception is treated as an error and details are sent to the
 * client. See {@linkplain Gateway} javadoc for the details on transaction
 * anatomy and exception semantics.
 * 
 * <p>
 * During the request processing the module can initiate communication with
 * other modules, send the response to the client or send a notification delayed
 * until the successful commit of the request processing transaction through the
 * {@linkplain Gateway}. See the {@code Gateway} javadoc for details on the
 * semantics of provided operations.
 * 
 * <p>
 * Modules can obtain various services through the Dependency Injection
 * mechanism. One way to use it is to declare a field with a type of one of the
 * injected services (from the list below), and annotate it with
 * 
 * <pre>
 * &#064;com.google.inject.Inject(optional = true)
 * </pre>
 * 
 * Reason for this strange declaration is purely technical and is a consequence
 * of an unconventional use of google's DI framework (guice).
 * 
 * <p>
 * Following types can be injected:
 * <ul>
 * <li>{@linkplain Config}
 * <li>{@linkplain Gateway}
 * <li>{@linkplain ServiceLocator}
 * <li>{@linkplain TransactionProvider}
 * <li>{@linkplain ModuleDescriptor} (of this module)
 * <li>Custom persistor (configurable type, inject using
 * {@linkplain CustomPersistor} annotation in addition to {@code Inject})
 * </ul>
 * 
 * <p>
 * Module configuration is contained in corresponding {@code ModuleDescriptor},
 * and global configuration through the {@code Config} object, both of which are
 * possible to obtain via the dependency injection mechanism described above.
 * There is, however, another way to obtain configuration properties. It is
 * possible to bind module's fields directly to their values, using
 * {@linkplain Named} together with {@linkplain Inject}, e.g.
 * 
 * <pre>
 * &#064;Inject
 * &#064;Named(&quot;buildings.max.quantity&quot;)
 * private int maxBuildings;
 * </pre>
 * 
 * Various types can be automatically converted from the string values contained
 * in the configuration file directly to the field type this way, providing a
 * convenient and general configuration mechanism.
 * 
 * @author los
 * @see ModuleBase
 * @see Context
 * @see Message
 */
public interface Module extends IModule {

    /**
     * Called in the first phase of moduleClass initailization, before the
     * communication environment is fully operational. Although the dispatcher
     * is already injected at this point, other modules may not be registered,
     * so no attempt of communication should be undertaken.
     */
    void init();

    /**
     * Called in the second phase of moduleClass initialization. The
     * communication environment is fully operational, it is valid to
     * communicate with other modules through the service locator mechanism
     * inside this method. Messages cannot be sent, since the initialization
     * sequence is not a transaction.
     */
    void started();

    /**
     * Called when a relevant message (uni/multicast) has been delivered to the
     * dispatcher.
     * 
     * @param message
     *            Message of interest (i.e. matching module's unicast address or
     *            one of its' multicast groups)
     * @param context
     *            Per-transaction general-purpose data store, intended as the
     *            stash to pass arbitrary information between modules.
     */
    void receive(Message message, Context context);

    /**
     * Called during the server's shutdown sequence. The order of module
     * shutdown is unspecified, and so no attempt to communicate with other
     * modules should be undertaken.
     */
    void shutdown();
}

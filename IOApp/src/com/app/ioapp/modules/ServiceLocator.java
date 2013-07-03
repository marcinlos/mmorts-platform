package com.app.ioapp.modules;

/**
 * Interface of a service manager, providing methods to register and lookup a
 * service. Designed to be used to ease inter-module communication, e.g. instead
 * of directly sending a message to other module, we can register this module as
 * a service, provide convenient interface and use simple message creating
 * wrapper as the provider.
 * 
 * <p>
 * Implementations should be thread safe. In particular,
 * {@linkplain #registerIfAbsent} must be atomic.
 * 
 */
public interface ServiceLocator {

    /**
     * Registers a service provider.
     * 
     * @param service
     *            Service interface
     * @param provider
     *            Service implementation
     */
    <T> void register(Class<? super T> service, T provider);

    /**
     * Registers a service provider if there is currently no provider of this
     * service.
     * 
     * @param service
     *            Service interface
     * @param provider
     *            Service implementation
     */
    <T> void registerIfAbsent(Class<? super T> service, T provider);

    /**
     * Looks up a specified service and return the implementation if the
     * provider is present and has the right type.
     * 
     * @param service
     *            Service to be found
     * @return Service provider, or {@code null} if none was found
     */
    <T> T lookup(Class<T> service);

}

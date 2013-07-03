package pl.edu.agh.ki.mmorts.client.communication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Simple, efficient {@linkplain ServiceLocator} implementation based on
 * concurrent hash map from the JDK.
 * 
 */
public class ServiceLocatorDelgate implements ServiceLocator {

    /** Internal provider map */
    private ConcurrentMap<Class<?>, Object> providers;

    public ServiceLocatorDelgate() {
        // no locking on retrieval, yet still allows dynamic insertion <3
        providers = new ConcurrentHashMap<Class<?>, Object>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void register(Class<? super T> service, T provider) {
        providers.put(service, provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void registerIfAbsent(Class<? super T> service, T provider) {
        providers.putIfAbsent(service, provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T lookup(Class<T> service) {
        Object o = providers.get(service);
        if (service.isInstance(o)) {
            return service.cast(o);
        } else {
            return null;
        }
    }

}

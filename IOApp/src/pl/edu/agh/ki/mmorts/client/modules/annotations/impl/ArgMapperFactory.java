package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import java.lang.reflect.Method;

/**
 * Creates mappers for methods (probably based on the signature, but perhaps
 * some other criteria may be taken into account). Represents a strategy of
 * argument mapping.
 * 
 */
public interface ArgMapperFactory {

    /**
     * Creates an argument mapper for a given method
     * 
     * @param method
     *            Method to map arguments for
     * @return Argument mapper performing mapping for the given method
     */
    ArgMapper newMapper(Method method);

}

package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import java.lang.reflect.Method;

/**
 * Argument mapper factory returning {@linkplain TrivialArgMapper} regardless of
 * passed method.
 * 
 */
public class TrivialMapperFactory implements ArgMapperFactory {

    private static final ArgMapper MAPPER = new TrivialArgMapper();

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Returns trivial mapper every time.
     */
    @Override
    public ArgMapper newMapper(Method method) {
        return MAPPER;
    }

}

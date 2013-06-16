package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import java.lang.reflect.Method;

public class TrivialMapperFactory implements ArgMapperFactory { 

    private static final ArgMapper MAPPER = new TrivialArgMapper();

    @Override
    public ArgMapper newMapper(Method method) {
        return MAPPER;
    }

}

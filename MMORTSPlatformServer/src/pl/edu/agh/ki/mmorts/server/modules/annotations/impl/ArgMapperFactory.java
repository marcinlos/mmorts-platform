package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import java.lang.reflect.Method;

public interface ArgMapperFactory {
    
    ArgMapper newMapper(Method method);

}

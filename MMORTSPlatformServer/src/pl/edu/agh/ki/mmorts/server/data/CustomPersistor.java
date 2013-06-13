package pl.edu.agh.ki.mmorts.server.data;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;

/**
 * Empty tag interface of a custom persistor.
 * 
 * <p>
 * Can have following dependncies injected:
 * <ul>
 * <li>{@linkplain Config} object, providing configuration properties
 * <li>{@linkplain Database} interface
 * </ul>
 * 
 * Supports {@linkplain OnInit} / {@linkplain OnShutdown} annotations for
 * initialization/cleanup.
 * 
 * <p>TODO: Think about it, I believe we don't need this interface at all.
 * 
 * @see pl.edu.agh.ki.mmorts.server.core.annotations.CustomPersistor
 */
@Deprecated
public interface CustomPersistor {

}

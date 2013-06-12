package pl.edu.agh.ki.mmorts.server.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation usually denoting cleanup method, which is supposed to be called
 * when the resources should be released. This is usually during the shutdown
 * sequence.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnShutdown {

}

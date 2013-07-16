package pl.edu.agh.ki.mmorts.client.backend.core.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * @author los
 */
@BindingAnnotation
@Target({ FIELD, METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
public @interface CustomPersistor {

}

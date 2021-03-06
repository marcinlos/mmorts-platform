package pl.edu.agh.ki.mmorts.server.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation usually denoting post-constructor initialization method, which is
 * to be called by the application after object creation, when non-constructor
 * injected dependencies are present.
 * 
 * @author los
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnInit {

}

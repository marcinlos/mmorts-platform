package pl.edu.agh.ki.mmorts.server.modules.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.edu.agh.ki.mmorts.common.message.Mode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageMapping {

    /** Request string */
    String[] value() default {};
    
    /** Mode of the message delivery */
    Mode[] mode() default Mode.UNICAST;

    /** Target address */
    String[] address() default {};
    
    /** Multicast group address */
    String[] group() default {};
    
    /** Source address */
    String[] source() default {};
    
    /** Carried data */
    Class<?>[] type() default {};
}

package kz.gov.pki.osgi.layer.annotations;

import java.lang.annotation.*;

/**
 * @author aslan
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NCALayerMethod {
	
	String name() default "";
	
}

package kz.gov.pki.osgi.layer.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author aslan
 *
 */

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface NCALayerClass {

}

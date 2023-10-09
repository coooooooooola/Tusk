package dispatch.run;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wanghaiqian on 17/2/21.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface ApiInfoAnnotation {
    String apiName() default "";
    String ctrlName() default "";
    String apiAuthor() default "";
    String env() default "prod";
}

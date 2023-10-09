package dispatch.run;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangyuedong on 2020/1/13.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface ThriftInfoAnnotation {
    String appkey() default "";
    String ctrlName() default "";
    String apiAuthor() default "";
    String env() default "prod";
}



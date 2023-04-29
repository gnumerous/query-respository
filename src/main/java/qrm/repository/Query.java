package qrm.repository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Query {
    String text() default "";
    Class<?> dataType() default HashMap.class;
}

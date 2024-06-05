package de.eztxm.annotation;

import de.eztxm.annotation.config.ConfigType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config {
    String path() default "";
    String fileName();
    ConfigType type();
}

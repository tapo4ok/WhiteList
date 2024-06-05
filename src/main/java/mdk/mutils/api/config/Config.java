package mdk.mutils.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String value() default "";

    Type type() default Config.Type.NONE;

    String FromResource() default "false";

    @Deprecated
    public static enum Type {
        JSON(".json"),
        @Deprecated
        YAML(".yml"),
        @Deprecated
        NONE(".invalid");

        public final String pa;

        private Type(String op) {
            this.pa = op;
        }
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Package {
        String value() default "";
    }
}
package org.trianglex.common.security.auth;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiAuthorization {
    String message() default "Sign error.";
}

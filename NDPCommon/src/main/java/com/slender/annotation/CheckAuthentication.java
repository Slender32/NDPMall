package com.slender.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD,TYPE})
@Retention(RUNTIME)
@Documented
public @interface CheckAuthentication {
}

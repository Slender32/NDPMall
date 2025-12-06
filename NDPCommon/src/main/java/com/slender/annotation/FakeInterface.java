package com.slender.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Documented
@Target({METHOD})
@Retention(SOURCE)
public @interface FakeInterface {
}

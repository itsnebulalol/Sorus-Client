package org.sorus.client.startup.injection.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {

  String value();

  String target() default "";

  Shift shift() default Shift.NULL;

  int ordinal() default -1;

  enum Shift {
    BEFORE,
    AFTER,
    NULL
  }
}

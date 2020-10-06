/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.startup.injection.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used with the {@link Inject} injection procedure. Used to specify the exact location
 * to inject into the method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {

  /**
   * Specifies what type of injection location the injection is. ex: "HEAD", "TAIL", "INVOKE"
   *
   * @return the injection location
   */
  String value();

  /**
   * Not required. Specifies the target of the injection location type.
   *
   * @return the target of the injection location type
   */
  String target() default "";

  /**
   * Specifies the {@link At.Shift} part of the injection location.
   *
   * @return the {@link At.Shift} of the injection location
   */
  Shift shift() default Shift.NULL;

  /**
   * Specifies which location should be injected into if there are more than one location that fit
   * the other arguments. -1 means that it will ignore the number of times it injects.
   *
   * @return the number of times the algorithm will find the location before actually injecting
   */
  int ordinal() default -1;

  /** Used to inject before or after a certain injection location. */
  enum Shift {
    BEFORE,
    AFTER,
    NULL
  }
}

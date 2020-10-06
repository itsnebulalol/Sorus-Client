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

package org.sorus.client.startup;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of the class data of each class, supplied by the {@link #transform(ClassLoader,
 * String, Class, ProtectionDomain, byte[])} method. Used for injection.
 */
public class ClassCollector implements ClassFileTransformer {

  /** Class data storage, including the class name, and the class file buffer */
  private static final Map<String, byte[]> originalClasses = new HashMap<>();

  private static final Map<String, byte[]> classes = new HashMap<>();

  /**
   * Adds class data to the storage if not already added.
   *
   * @param loader the class loader
   * @param className the name of the class
   * @param classBeingRedefined the {@link java.lang.Class} of the class being transformed
   * @param protectionDomain the protection domain
   * @param classfileBuffer the data for the class file
   * @return the modified class file data-
   */
  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    if (!originalClasses.containsKey(className)) {
      originalClasses.put(className, classfileBuffer);
      classes.put(className, classfileBuffer);
    }
    return new byte[0];
  }

  public static byte[] getOriginalData(String className) {
    return originalClasses.get(className);
  }

  public static byte[] getData(String className) {
    return classes.get(className);
  }

  public static void setData(String className, byte[] data) {
    classes.put(className, data);
  }
}

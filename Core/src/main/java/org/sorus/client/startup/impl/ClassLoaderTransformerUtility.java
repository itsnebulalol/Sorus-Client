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

package org.sorus.client.startup.impl;

import java.net.URLClassLoader;
import org.sorus.client.startup.TransformerUtility;

/** Implementation of {@link TransformerUtility} for a class loader injection. */
public class ClassLoaderTransformerUtility extends TransformerUtility {

  private URLClassLoader classLoader;

  /** Initializes the utility. */
  @Override
  public void initialize() {}

  /**
   * Transforms the class.
   *
   * @param className the class being transformed
   * @param data the class' data
   * @return the modified class' data
   */
  public byte[] transform(String className, byte[] data, ClassLoader classLoader) {
    return this.transformer.transform(className, data, classLoader, null);
  }

  public void setClassLoader(URLClassLoader classLoader) {
    this.classLoader = classLoader;
  }
}

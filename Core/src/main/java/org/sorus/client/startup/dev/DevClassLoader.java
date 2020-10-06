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

package org.sorus.client.startup.dev;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import org.sorus.client.startup.impl.ClassLoaderTransformerUtility;

/** Class loader used to interface with Sorus's rendering system and transform minecraft classes. */
public class DevClassLoader extends URLClassLoader {

  private final ClassLoaderTransformerUtility transformerUtility;

  public DevClassLoader(URL[] urls, ClassLoaderTransformerUtility transformerUtility) {
    super(urls, null);
    this.transformerUtility = transformerUtility;
    transformerUtility.setClassLoader(this);
  }

  /**
   * Gets the class by getting its original byte code and then transforming it based on the
   * transformer.
   *
   * @param name the class name being loaded
   * @return the class being loaded
   */
  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] bt = loadClassData(name);
    if (bt == null) {
      return super.findClass(name);
    }
    bt = transformerUtility.transform(name.replace(".", "/"), bt, this);
    return defineClass(name, bt, 0, bt.length);
  }

  /**
   * Gets the default class data for a certain class.
   *
   * @param className the class being loaded
   * @return the default class data
   */
  private byte[] loadClassData(String className) {
    InputStream is =
        getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
    if (is == null) {
      return null;
    }
    ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
    int len;
    try {
      while ((len = is.read()) != -1) {
        byteSt.write(len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return byteSt.toByteArray();
  }
}

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

import org.sorus.client.Sorus;
import org.sorus.client.version.IVersion;

import java.util.Map;

/** Used to start Sorus using a specific class loader. */
public class SorusStartupCustomClassLoader {

  /**
   * Starts sorus, proving the given version class.
   *
   * @param versionClass the version class implementation
   */
  public static void start(String versionClass, Map<String, String> args, boolean dev) {
    SorusStartup.DEV = dev;
    SorusStartup.setLaunchArgs(args);
    Sorus sorus = Sorus.getSorus();
    try {
      sorus.initialize(
          (Class<? extends IVersion>) Class.forName(versionClass), SorusStartup.getLaunchArgs());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}

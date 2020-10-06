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

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.detection.InjectorInfo;
import org.sorus.client.version.IVersion;

/** Class that allows for startup from any version implementation of Sorus. */
public class SorusStartup {

  public static boolean DEV;

  public static ClassLoader classLoader;

  public static TransformerUtility transformer;

  private static final Map<String, String> launchArgs = new HashMap<>();

  /**
   * Allows startup of Sorus from any version implementation. Also starts the class data collector
   * to get data from all the loaded classes for later injection use.
   *
   * @param version the version class used for connecting with the specific version of minecraft
   * @param transformerUtility the instrumentation instance
   * @param classLoader the classloader to use when starting Sorus
   * @param dev if the environment is a dev environment
   */
  public static void start(
      Class<? extends IVersion> version,
      TransformerUtility transformerUtility,
      ClassLoader classLoader,
      String args,
      boolean dev) {
    if (args != null) {
      for (String string : args.split(",")) {
        String[] strings = string.split("=");
        launchArgs.put(strings[0], strings[1]);
      }
    }
    DEV = dev;
    transformer = transformerUtility;
    transformer.initialize();
    SorusStartup.registerTransformers(transformer);
    new Thread(
            () -> {
              ClassLoader classLoader1 = classLoader;
              if (classLoader1 == null) {
                while (SorusStartup.classLoader == null) {
                  System.out.print("");
                }
                classLoader1 = SorusStartup.classLoader;
              }
              try {
                final Class<?> clazz =
                    Class.forName(
                        "org.sorus.client.startup.SorusStartupCustomClassLoader",
                        false,
                        classLoader1);
                final Method mainMethod =
                    clazz.getMethod("start", String.class, String.class, boolean.class);
                mainMethod.invoke(null, version.getName(), args, dev);
              } catch (ClassNotFoundException
                  | NoSuchMethodException
                  | IllegalAccessException
                  | InvocationTargetException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * Registers injectors and adds then to a list of injectors for each class.
   *
   * @param transformer the {@link TransformerUtility} to register injectors with
   */
  private static void registerTransformers(TransformerUtility transformer) {
    Map<String, List<Class<?>>> injectors = new HashMap<>();
    InputStream inputStream =
        SorusStartup.class.getClassLoader().getResourceAsStream("sorus.injectors.json");
    if (inputStream == null) {
      return;
    }
    try {
      String injectorJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      Gson gson = new Gson();
      InjectorInfo info = gson.fromJson(injectorJson, InjectorInfo.class);
      String location = info.location;
      for (String string : info.injectors) {
        String fullClassName = (location + (location.isEmpty() ? "" : ".") + string);
        Class<?> injector = Class.forName(fullClassName);
        String hookClass =
            ObfuscationManager.getClassName(injector.getDeclaredAnnotation(Hook.class).value());
        injectors.computeIfAbsent(hookClass, k -> new ArrayList<>()).add(injector);
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    transformer.addTransformer(new MainTransformer(injectors));
  }

  public static Map<String, String> getLaunchArgs() {
    return launchArgs;
  }
}

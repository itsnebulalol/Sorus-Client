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

package org.sorus.client.startup.injection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.IOUtils;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.Info;
import org.sorus.client.startup.SorusStartup;

public class InjectionManager {

  private static final Map<Class<?>, List<Class<?>>> injectorClasses = new HashMap<>();
  private static final BiMap<Object, List<Object>> injectors = HashBiMap.create();

  static {
    String version = SorusStartup.getLaunchArgs().get("version");
    if (SorusStartup.getLaunchArgs().get("plugins") != null) {
      for (String string : SorusStartup.getLaunchArgs().get("plugins").split(",")) {
        String actual = string.substring(2);
        switch (string.charAt(0)) {
          case 'p':
            File directory = new File(actual);
            for (File file : Objects.requireNonNull(directory.listFiles())) {
              File file1 = new File(file, version);
              if (file1.exists()) {
                try {
                  File file2 = new File(file1, version + ".jar");
                  Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                  method.setAccessible(true);
                  method.invoke(
                      SorusStartup.class.getClassLoader(),
                      (file1.exists()
                              ? new File(file1, file1.getName() + ".jar")
                              : new File(file, file.getName() + ".jar"))
                          .toURI()
                          .toURL());
                  JarFile jarFile = new JarFile(file2);
                  JarEntry jarEntry = jarFile.getJarEntry(version + ".json");
                  InputStream inputStream = jarFile.getInputStream(jarEntry);
                  Scanner scanner = new Scanner(inputStream);
                  StringBuilder stringBuilder = new StringBuilder();
                  while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                  }
                  Info info = new Gson().fromJson(stringBuilder.toString(), Info.class);
                  String location = info.location;
                  for (String injectorName : info.injectors) {
                    String fullClassName =
                        (location + (location.isEmpty() ? "" : ".") + injectorName);
                    Class<?> injector = Class.forName(fullClassName);
                    String hookClass =
                        ObfuscationManager.getClassName(
                            injector.getDeclaredAnnotation(Hook.class).value());
                    injectorClasses
                        .computeIfAbsent(
                            Class.forName(hookClass.replace("/", ".")), k -> new ArrayList<>())
                        .add(injector);
                  }
                } catch (IOException
                    | ClassNotFoundException
                    | NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                  e.printStackTrace();
                }
              }
            }
            break;
        }
      }
    }
    InputStream pluginVersionJson =
        InjectionManager.class.getClassLoader().getResourceAsStream(version + ".json");
    if (pluginVersionJson != null) {
      try {
        String json = IOUtils.toString(pluginVersionJson, StandardCharsets.UTF_8);
        Info info = new Gson().fromJson(json, Info.class);
        String location = info.location;
        for (String string : info.injectors) {
          String fullClassName = (location + (location.isEmpty() ? "" : ".") + string);
          Class<?> injector = Class.forName(fullClassName);
          String hookClass =
              ObfuscationManager.getClassName(injector.getDeclaredAnnotation(Hook.class).value());
          injectorClasses
              .computeIfAbsent(Class.forName(hookClass.replace("/", ".")), k -> new ArrayList<>())
              .add(injector);
        }
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    InputStream inputStream =
        InjectionManager.class.getClassLoader().getResourceAsStream("sorus.json");
    try {
      assert inputStream != null;
      String injectorJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      Gson gson = new Gson();
      Info info = gson.fromJson(injectorJson, Info.class);
      String location = info.location;
      for (String string : info.injectors) {
        String fullClassName = (location + (location.isEmpty() ? "" : ".") + string);
        Class<?> injector = Class.forName(fullClassName);
        String hookClass =
            ObfuscationManager.getClassName(injector.getDeclaredAnnotation(Hook.class).value());
        injectorClasses
            .computeIfAbsent(Class.forName(hookClass.replace("/", ".")), k -> new ArrayList<>())
            .add(injector);
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Object getInjector(Object object, String injectorClassString) {
    if (object == null) {
      return null;
    }
    if (getInjectorInternal(object, injectorClassString) == null) {
      try {
        Class<?> clazz = object.getClass();
        while (InjectionManager.keepSearchingForSuperClass(clazz, injectorClassString)) {
          clazz = clazz.getSuperclass();
        }
        List<Class<?>> injectorClasses = InjectionManager.injectorClasses.get(clazz);
        for (Class<?> injectorClass : injectorClasses) {
          if (injectorClass.getCanonicalName().equals(injectorClassString)) {
            Object injector = injectorClass.getConstructors()[0].newInstance(object);
            injectors.computeIfAbsent(object, k -> new ArrayList<>()).add(injector);
          }
        }
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return getInjectorInternal(object, injectorClassString);
  }

  private static boolean keepSearchingForSuperClass(Class<?> clazz, String injectorClassString) {
    if (injectorClasses.get(clazz) == null) {
      return true;
    }
    for (Class<?> injectorClass : injectorClasses.get(clazz)) {
      if (injectorClass.getCanonicalName().equals(injectorClassString)) {
        return false;
      }
    }
    return true;
  }

  private static Object getInjectorInternal(Object object, String injectorClass) {
    if (injectors.get(object) == null) {
      return null;
    }
    for (Object injector : injectors.get(object)) {
      if (injector.getClass().getCanonicalName().equals(injectorClass)) {
        return injector;
      }
    }
    return null;
  }
}

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

package org.sorus.client.plugin;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarFile;

public class PluginManager {

  private final List<IPlugin> loadedPlugins = new ArrayList<>();

  public void register(IPlugin plugin) {
    this.loadedPlugins.add(plugin);
    plugin.onLoad();
  }

  public void register(File pluginJar) {
    ClassLoader classLoader = PluginManager.class.getClassLoader();
    try {
      URL url = pluginJar.toURI().toURL();
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(classLoader, url);
    } catch (MalformedURLException
        | NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    try {
      JarFile jarFile = new JarFile(pluginJar);
      InputStream inputStream = jarFile.getInputStream(jarFile.getJarEntry("plugin.json"));
      StringBuilder stringBuilder = new StringBuilder();
      Scanner scanner = new Scanner(inputStream);
      while (scanner.hasNextLine()) {
        stringBuilder.append(scanner.nextLine());
      }
      PluginJSON pluginJson = new Gson().fromJson(stringBuilder.toString(), PluginJSON.class);
      Class<? extends IPlugin> pluginClass =
          (Class<? extends IPlugin>) Class.forName(pluginJson.getMain());
      IPlugin plugin = pluginClass.newInstance();
      this.register(plugin);
    } catch (IOException
        | ClassNotFoundException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

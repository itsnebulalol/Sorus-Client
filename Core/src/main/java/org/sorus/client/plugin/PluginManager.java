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
import java.util.Objects;
import java.util.Scanner;
import org.sorus.client.Sorus;
import org.sorus.client.startup.SorusStartup;

public class PluginManager {

  private final List<IPlugin> loadedPlugins = new ArrayList<>();

  public void initialize(String plugins) {
    if (plugins == null) {
      return;
    }
    for (String string : plugins.split(",")) {
      String actual = string.substring(2);
      switch (string.charAt(0)) {
        case 'p':
          File directory = new File(actual);
          for (File file : Objects.requireNonNull(directory.listFiles())) {
            this.register(file);
          }
          break;
        case 'c':
          try {
            this.register((IPlugin) Class.forName(actual).newInstance());
          } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
          }
          break;
      }
    }
  }

  public void register(IPlugin plugin) {
    this.loadedPlugins.add(plugin);
    plugin.onLoad();
  }

  public void register(File pluginFile) {
    File file1 = new File(pluginFile, Sorus.getSorus().getArgs().get("version"));
    try {
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(
          SorusStartup.class.getClassLoader(),
          new File(pluginFile, pluginFile.getName() + ".jar").toURI().toURL());
      if (file1.exists()) {
        try {
          method.invoke(
              SorusStartup.class.getClassLoader(),
              new File(file1, file1.getName() + ".jar").toURI().toURL());
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
      File file2 = new File(pluginFile, pluginFile.getName() + ".json");
      try {
        InputStream inputStream = file2.toURI().toURL().openStream();
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
          stringBuilder.append(scanner.nextLine());
        }
        PluginJSON pluginJson = new Gson().fromJson(stringBuilder.toString(), PluginJSON.class);
        Class<?> pluginClass = Class.forName(pluginJson.getMain());
        this.register((IPlugin) pluginClass.newInstance());
      } catch (IOException
          | InstantiationException
          | IllegalAccessException
          | ClassNotFoundException e) {
        e.printStackTrace();
      }
    } catch (NoSuchMethodException
        | IllegalAccessException
        | InvocationTargetException
        | MalformedURLException e) {
      e.printStackTrace();
    }
  }
}

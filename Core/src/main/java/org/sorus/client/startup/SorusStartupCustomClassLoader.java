package org.sorus.client.startup;

import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.version.IVersion;

public class SorusStartupCustomClassLoader {

  public static void start(String versionClass, Map<String, String> args, boolean dev) {
    SorusStartup.DEV = dev;
    SorusStartup.setLaunchArgs(args);
    Sorus sorus = Sorus.getSorus();
    try {
      sorus.initialize(
          (Class<? extends IVersion>) Class.forName(versionClass), SorusStartup.getArgsMap());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}

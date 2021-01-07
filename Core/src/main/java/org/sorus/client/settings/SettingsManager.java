package org.sorus.client.settings;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;
import sun.misc.IOUtils;

public class SettingsManager implements ISettingHolder, IConfigurableScreen {

  private final List<ISettingHolder> settings = new ArrayList<>();
  private String currentProfile = "default";

  private final List<Setting<?>> clientSettings = new ArrayList<>();

  private final Setting<Input> openGui;

  public SettingsManager() {
    this.register(this);
    this.register(openGui = new Setting<>("openGui", Key.SHIFT_RIGHT));
  }

  public void register(ISettingHolder settingHolder) {
    this.settings.add(settingHolder);
  }

  private void register(Setting<?> setting) {
    this.clientSettings.add(setting);
  }

  public void save() {
    new Thread(
            () -> {
              File sorus = new File("sorus");
              File settings = new File(sorus, "settings");
              File profile = new File(settings, currentProfile);
              profile.mkdirs();
              for (ISettingHolder setting : SettingsManager.this.settings) {
                File file =
                    new File(
                        profile,
                        setting.getSettingsName().toLowerCase().replace(" ", "_") + ".set");
                OutputStream output = null;
                try {
                  output = new FileOutputStream(file.getPath());
                  output.write(
                      JsonWriter.formatJson(JsonWriter.objectToJson(setting.getSettings()))
                          .getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                  e.printStackTrace();
                } finally {
                  try {
                    if (output != null) {
                      output.flush();
                      output.close();
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              }
            })
        .start();
  }

  public void load(String profileName) {
    this.currentProfile = profileName;
    File sorus = new File("sorus");
    File settings = new File(sorus, "settings");
    File profile = new File(settings, currentProfile);
    profile.mkdirs();
    for (ISettingHolder setting : SettingsManager.this.settings) {
      File file =
          new File(profile, setting.getSettingsName().toLowerCase().replace(" ", "_") + ".set");
      if (!file.exists()) {
        continue;
      }
      try {
        InputStream input = new FileInputStream(file);
        Object loadedSettings =
            JsonReader.jsonToJava(new String(IOUtils.readFully(input, -1, true)));
        setting.setSettings(loadedSettings);
        input.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getCurrentProfile() {
    return currentProfile;
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(openGui, "Client GUI Keybind"));
  }

  @Override
  public String getDisplayName() {
    return "Settings";
  }

  public Input getOpenGuiKeybind() {
    return openGui.getValue();
  }

  @Override
  public Map<String, Object> getSettings() {
    Map<String, Object> settings = new HashMap<>();
    for (Setting<?> setting : this.clientSettings) {
      settings.put(setting.getName(), setting.getValue());
    }
    return settings;
  }

  @Override
  public void setSettings(Object settings) {
    Map<String, Object> settingsMap = (Map<String, Object>) settings;
    boolean continuing = true;
    int i = 0;
    if (this.clientSettings.size() == 0) {
      continuing = false;
    }
    while (continuing) {
      Setting<?> setting = this.clientSettings.get(i);
      if (settingsMap.get(setting.getName()) != null) {
        setting.setValueIgnoreType(settingsMap.get(setting.getName()));
      }
      i++;
      if (i == this.clientSettings.size()) {
        continuing = false;
      }
    }
  }

  @Override
  public String getSettingsName() {
    return "settings";
  }
}

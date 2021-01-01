package org.sorus.client.settings;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SettingsManager {

  private final List<ISettingHolder> settings = new ArrayList<>();
  private String currentProfile = "default";

  public void register(ISettingHolder settingHolder) {
    this.settings.add(settingHolder);
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
                try {
                  FileWriter fileWriter = new FileWriter(file);
                  fileWriter.write(
                      JsonWriter.formatJson(JsonWriter.objectToJson(setting.getSettings())));
                  fileWriter.close();
                } catch (IOException e) {
                  e.printStackTrace();
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
        Scanner scanner = new Scanner(file);
        StringBuilder text = new StringBuilder();
        while (scanner.hasNextLine()) {
          text.append(scanner.nextLine());
        }

        Object loadedSettings = JsonReader.jsonToJava(text.toString());
        setting.setSettings(loadedSettings);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public String getCurrentProfile() {
    return currentProfile;
  }
}

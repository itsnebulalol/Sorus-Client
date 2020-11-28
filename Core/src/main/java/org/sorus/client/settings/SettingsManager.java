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

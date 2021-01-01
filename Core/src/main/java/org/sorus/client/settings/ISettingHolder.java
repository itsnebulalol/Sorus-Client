package org.sorus.client.settings;

public interface ISettingHolder {

  Object getSettings();

  void setSettings(Object settings);

  String getSettingsName();
}

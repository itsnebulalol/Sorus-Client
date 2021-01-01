package org.sorus.client.settings;

public class Setting<T> {

  private final String name;

  private T value;

  public Setting(String name, T defaultValue) {
    this.name = name;
    this.value = defaultValue;
  }

  public String getName() {
    return name;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public void setValueIgnoreType(Object value) {
    this.setValue((T) value);
  }
}

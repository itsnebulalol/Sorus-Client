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

package org.sorus.client.gui.hud;

import java.util.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.settings.ISettingHolder;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.IScreen;

/** Contains components and it used for rendering in game huds. */
public class HUD implements ISettingHolder {

  private final Setting<String> name;

  private final List<Setting<?>> settings = new ArrayList<>();

  /** Information about the current HUD. */
  private final Setting<HUDPosition> position;

  private final Setting<Double> scale;

  /** The components that have been added to the hud. */
  private final Setting<List<IComponent>> components;

  public HUD() {
    this.register(name = new Setting<>("name", null));
    this.register(position = new Setting<>("position", new HUDPosition()));
    this.register(scale = new Setting<>("scale", 1.0));
    components = new Setting<>("components", new ArrayList<>());
  }

  /** Registers a setting by adding it to the list of registered settings. */
  private void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  public void addComponent(IComponent component) {
    this.components.getValue().add(component);
    component.setHUD(this);
    component.onAdd();
  }

  public void removeComponent(Component component) {
    this.components.getValue().remove(component);
    component.onRemove();
  }

  /**
   * Renders the hud by going through all the components and rendering them at their correct
   * positions.
   */
  public void render() {
    List<IComponent> components = this.components.getValue();
    boolean dummy = Sorus.getSorus().getGUIManager().isScreenOpen(HUDPositionScreen.class);
    for (IComponent component : components) {
      component.update(dummy);
    }
    double x = this.getLeft();
    double y = this.getTop();
    IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
    glHelper.translate(x, y, 0);
    double scale = this.scale.getValue();
    glHelper.scale(scale, scale, 1);
    glHelper.translate(-x, -y, 0);
    double yOffset = 0;
    for (IComponent component : this.components.getValue()) {
      component.render(x, y + yOffset, dummy);
      yOffset += component.getHeight();
    }
    glHelper.translate(x, y, 0);
    glHelper.scale(1 / scale, 1 / scale, 1);
    glHelper.translate(-x, -y, 0);
  }

  /**
   * Gets the x position of the hud.
   *
   * @return the x position
   */
  public double getX() {
    return this.position
        .getValue()
        .getX(this, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth());
  }

  /** Sets the x position of the hud. */
  public void setX(double x) {
    this.position
        .getValue()
        .setX(this, x, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth());
  }

  /**
   * Gets the y position of the hud.
   *
   * @return the y position
   */
  public double getY() {
    return this.position
        .getValue()
        .getY(this, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight());
  }

  /** Sets the y position of the hud. */
  public void setY(double y) {
    this.position
        .getValue()
        .setY(this, y, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight());
  }

  /**
   * Gets the scale of a hud.
   *
   * @return the scale
   */
  public double getScale() {
    return scale.getValue();
  }

  /** Sets the y scale of the hud. */
  public void setScale(double scale) {
    this.scale.setValue(scale);
  }

  /**
   * Gets the unscaled width of a hud.
   *
   * @return the unscaled width
   */
  public double getWidth() {
    double width = 0;
    for (IComponent component : components.getValue()) {
      width = Math.max(width, component.getWidth());
    }
    return width;
  }

  /**
   * Gets the scaled width.
   *
   * @return the scaled width
   */
  public double getScaledWidth() {
    return this.getWidth() * this.getScale();
  }

  /**
   * Gets the unscaled height of a hud.
   *
   * @return the unscaled height
   */
  public double getHeight() {
    double height = 0;
    for (IComponent component : components.getValue()) {
      height = height + component.getHeight();
    }
    return height;
  }

  /**
   * Gets the scaled height.
   *
   * @return the scaled width
   */
  public double getScaledHeight() {
    return this.getHeight() * this.getScale();
  }

  /**
   * Gets the left edge of the hud.
   *
   * @return the left location edge
   */
  public double getLeft() {
    return this.getX() - this.getScaledWidth() / 2;
  }

  /**
   * Gets the right edge location of the hud.
   *
   * @return the right edge
   */
  public double getRight() {
    return this.getX() + this.getScaledWidth() / 2;
  }

  /**
   * Gets the top edge location of the hud.
   *
   * @return the top edge
   */
  public double getTop() {
    return this.getY() - this.getScaledHeight() / 2;
  }

  /**
   * Gets the bottom edge location of the hud.
   *
   * @return the bottom edge
   */
  public double getBottom() {
    return this.getY() + this.getScaledHeight() / 2;
  }

  public String getName() {
    return name.getValue();
  }

  public void setName(String name) {
    this.name.setValue(name);
  }

  /** Called when a hud is first created. Mainly used to display a menu to edit the hud. */
  public void onCreation() {
    this.setName("HUD");
    Sorus.getSorus().getGUIManager().open(new HUDConfigScreen(this));
  }

  /** Gets the description of the hud, used in guis. */
  public String getDescription() {
    return "";
  }

  /** Displays the main settings menu for the hud. */
  public void displaySettings(Screen currentScreen) {
    Sorus.getSorus().getGUIManager().open(new HUDConfigScreen(this));
  }

  public List<IComponent> getComponents() {
    return components.getValue();
  }

  @Override
  public Object getSettings() {
    Map<String, Object> hudSettings = new HashMap<>();
    LinkedHashMap<Class<?>, Object> componentSettings = new LinkedHashMap<>();
    for (IComponent component : this.components.getValue()) {
      componentSettings.put(component.getClass(), component.getSettings());
    }
    hudSettings.put("components", componentSettings);
    for (Setting<?> setting : this.settings) {
      hudSettings.put(setting.getName(), setting.getValue());
    }
    return hudSettings;
  }

  @Override
  public void setSettings(Object settings) {
    Map<String, Object> settingsMap = (Map<String, Object>) settings;
    for (Setting<?> setting : this.settings) {
      if (settingsMap.get(setting.getName()) != null) {
        setting.setValueIgnoreType(settingsMap.get(setting.getName()));
      }
    }
    LinkedHashMap<Class<? extends Component>, Map<String, String>> componentSettings =
        (LinkedHashMap<Class<? extends Component>, Map<String, String>>)
            settingsMap.get("components");
    for (Class<? extends Component> componentClass : componentSettings.keySet()) {
      try {
        Component component = componentClass.newInstance();
        component.setSettings(componentSettings.get(componentClass));
        this.addComponent(component);
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getSettingsName() {
    return this.getName();
  }

  public List<IComponent> getHUDComponents() {
    return this.components.getValue();
  }
}

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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.module.impl.chat.ChatComponent;
import org.sorus.client.module.impl.cps.CPSComponent;
import org.sorus.client.module.impl.fps.FPSComponent;
import org.sorus.client.module.impl.gps.GPSComponent;
import org.sorus.client.module.impl.keystrokes.KeystrokesComponent;
import org.sorus.client.module.impl.ping.PingComponent;
import org.sorus.client.settings.ISettingHolder;
import org.sorus.client.version.input.Key;

public class HUDManager implements ISettingHolder {

  private final List<Class<? extends Component>> registeredComponents;

  private final List<HUD> huds;

  public HUDManager() {
    this.registeredComponents = new ArrayList<>();
    this.huds = new ArrayList<>();
  }

  public void initialize() {
    Sorus.getSorus().getEventManager().register(this);
    this.registerInternalComponents();
    Sorus.getSorus().getSettingsManager().register(this);
  }

  /** Registers all the default available components. */
  public void registerInternalComponents() {
    this.registerComponent(FPSComponent.class);
    this.registerComponent(CPSComponent.class);
    this.registerComponent(GPSComponent.class);
    this.registerComponent(KeystrokesComponent.class);
    this.registerComponent(ChatComponent.class);
    this.registerComponent(PingComponent.class);
  }

  /** Adds a component as one of the available components for adding to a hud. */
  public void registerComponent(Class<? extends Component> component) {
    this.registeredComponents.add(component);
  }

  public void register(HUD hud) {
    this.huds.add(hud);
  }

  public void unregister(HUD hud) {
    this.huds.remove(hud);
    for (IComponent component : hud.getHUDComponents()) {
      component.onRemove();
    }
  }

  public List<HUD> getHUDs() {
    return huds;
  }

  public List<Class<? extends Component>> getRegisteredComponents() {
    return registeredComponents;
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (Sorus.getSorus().getVersion().getGame().isIngame() && e.getKey() == Key.SHIFT_RIGHT) {
      Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(this));
    }
  }

  @Override
  public List<Pair<Class<? extends HUD>, ?>> getSettings() {
    List<Pair<Class<? extends HUD>, ?>> settings = new ArrayList<>();
    for (HUD hud : this.huds) {
      settings.add(Pair.of(hud.getClass(), hud.getSettings()));
    }
    return settings;
  }

  @Override
  public void setSettings(Object settings) {
    List<Pair<Class<? extends HUD>, ?>> settingsList =
        (List<Pair<Class<? extends HUD>, ?>>) settings;
    for (Pair<Class<? extends HUD>, ?> pair : settingsList) {
      try {
        HUD hud = pair.getKey().newInstance();
        hud.setSettings(pair.getValue());
        this.register(hud);
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getSettingsName() {
    return "hud";
  }
}

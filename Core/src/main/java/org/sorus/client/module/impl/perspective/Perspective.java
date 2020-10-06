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

package org.sorus.client.module.impl.perspective;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.KeyReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.PerspectiveMode;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class Perspective extends ModuleConfigurable {

  private final Setting<Input> keybind;

  private boolean toggled;
  private PerspectiveMode savedPerspective;
  private float rotationPitch, rotationYaw;

  public Perspective() {
    super("PERSPECTIVE");
    this.register(keybind = new Setting<>("keybind", Key.F));
  }

  @Override
  public void onLoad() {
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (this.isEnabled()
        && e.getKey().equals(keybind.getValue())
        && !toggled
        && Sorus.getSorus().getVersion().getGame().isIngame()) {
      toggled = true;
      savedPerspective = Sorus.getSorus().getVersion().getGame().getPerspective();
      Sorus.getSorus().getVersion().getGame().setPerspective(PerspectiveMode.THIRD_PERSON_BACK);
      if (savedPerspective == PerspectiveMode.THIRD_PERSON_FRONT) {
        rotationYaw += 180;
        rotationPitch = -rotationPitch;
      }
    }
  }

  @EventInvoked
  public void onKeyRelease(KeyReleaseEvent e) {
    if (this.isEnabled() && e.getKey().equals(keybind.getValue()) && toggled) {
      toggled = false;
      Sorus.getSorus().getVersion().getGame().setPerspective(savedPerspective);
    }
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(keybind, "Perspective Toggle"));
  }

  @Override
  public String getDescription() {
    return "Look around your character like in F5 without turning your character.";
  }

  public boolean isToggled() {
    return this.isEnabled() && toggled;
  }

  public float getRotationPitch() {
    return rotationPitch;
  }

  public float getRotationYaw() {
    return rotationYaw;
  }

  public void setRotationPitch(float rotationPitch) {
    this.rotationPitch = rotationPitch;
  }

  public void setRotationYaw(float rotationYaw) {
    this.rotationYaw = rotationYaw;
  }
}

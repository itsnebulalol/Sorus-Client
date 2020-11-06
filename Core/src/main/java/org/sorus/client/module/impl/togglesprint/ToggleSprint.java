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

package org.sorus.client.module.impl.togglesprint;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.input.KeybindType;

public class ToggleSprint extends ModuleConfigurable {

  private final Setting<Input> toggleSprint;

  private boolean toggled;

  public ToggleSprint() {
    super("TOGGLE SPRINT");
    this.register(toggleSprint = new Setting<>("toggleSprint", Key.R));
  }

  @Override
  public void onLoad() {
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (this.isEnabled()
        && Sorus.getSorus().getVersion().getGame().isIngame()
        && e.getKey().equals(toggleSprint.getValue())) {
      toggled = !toggled;
      Sorus.getSorus().getVersion().getInput().getKeybind(KeybindType.SPRINT).setState(toggled);
    }
    if (e.getKey()
        .equals(Sorus.getSorus().getVersion().getInput().getKeybind(KeybindType.SPRINT).getKey())) {
      toggled = false;
    }
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(toggleSprint, "Toggle Sprint"));
  }
}

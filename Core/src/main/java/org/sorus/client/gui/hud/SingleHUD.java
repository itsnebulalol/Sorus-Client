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

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.gui.screen.settings.SettingsScreen;

/**
 * Implementation of {@link HUD} for a hud that will only contain one module for easier
 * configuration.
 */
public class SingleHUD extends HUD {

  /** Overrides the method so the hud can only have one component at a time. */
  @Override
  public void addComponent(IComponent component) {
    this.getComponents().clear();
    this.getComponents().add(component);
    component.setHUD(this);
    component.onAdd();
  }

  @Override
  public void onCreation() {
    Sorus.getSorus()
        .getGUIManager()
        .open(
            new SelectComponentScreen(Sorus.getSorus().getHUDManager(), new OnSelectScreenExit()));
  }

  @Override
  public void displaySettings(Screen currentScreen) {
    Sorus.getSorus()
        .getGUIManager()
        .open(new SettingsScreen(currentScreen, (IConfigurableScreen) this.getComponents().get(0)));
  }

  @Override
  public String getDescription() {
    return ((Component) this.getComponents().get(0)).getDescription();
  }

  public class OnSelectScreenExit implements Callback<IComponent> {

    @Override
    public void call(IComponent selected) {
      SingleHUD.this.addComponent(selected);
      SingleHUD.this.setName(selected.getName());
      SingleHUD.this.displaySettings(new HUDListScreen());
    }

    @Override
    public void cancel() {
      Sorus.getSorus().getHUDManager().unregister(SingleHUD.this);
    }
  }
}

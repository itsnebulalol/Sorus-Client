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

package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.GUIManager;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.HUDRenderScreen;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.theme.ThemeBase;

public class DefaultHUDRenderScreen extends ThemeBase<HUDRenderScreen> {

  private final HUDManager hudManager;

  public DefaultHUDRenderScreen(HUDManager hudManager) {
    this.hudManager = hudManager;
  }

  @Override
  public void render() {
    if (this.shouldRenderHUDS()) {
      for (HUD hud : this.hudManager.getHUDs()) {
        hud.render();
      }
    }
  }

  private boolean shouldRenderHUDS() {
    Sorus sorus = Sorus.getSorus();
    GUIManager guiManager = sorus.getGUIManager();
    return sorus.getVersion().getGame().shouldRenderHUDS()
        && !guiManager.isScreenOpen(HUDPositionScreen.class) && sorus.getVersion().getGame().getPlayer().exists();
  }
}

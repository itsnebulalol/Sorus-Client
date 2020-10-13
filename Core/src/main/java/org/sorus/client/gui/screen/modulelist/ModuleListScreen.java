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

package org.sorus.client.gui.screen.modulelist;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.module.ModuleConfigurable;

public class ModuleListScreen extends ThemeableScreen {

  public ModuleListScreen() {
    super(
        Sorus.getSorus()
            .getThemeManager()
            .getTheme("module-list", Sorus.getSorus().getModuleManager()));
  }

  public void displayModuleSettings(ModuleConfigurable module) {
    Sorus.getSorus().getGUIManager().close(this);
    Sorus.getSorus().getGUIManager().open(new SettingsScreen(module));
  }

  public void enableDisableModule(ModuleConfigurable module, boolean enable) {
    module.setEnabled(enable);
  }

  @Override
  public boolean shouldTakeOutOfGame() {
    return true;
  }
}

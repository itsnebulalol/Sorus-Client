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

package org.sorus.client.gui.core;

import org.sorus.client.gui.theme.ITheme;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

public class ThemeableScreen extends Screen {

  private final ITheme<?> theme;

  public ThemeableScreen(ITheme<ThemeableScreen> theme) {
    this.theme = theme;
    theme.setScreen(this);
  }

  @Override
  public void onOpen() {
    this.theme.init();
  }

  @Override
  public void onRender() {
    this.theme.render();
  }

  @Override
  public void onExit() {
    this.theme.exit();
  }

  @Override
  public void keyTyped(Key key) {
    this.theme.keyTyped(key);
  }

  @Override
  public void mouseClicked(Button button, double x, double y) {
    this.theme.mouseClicked(button, x, y);
  }

  @Override
  public void mouseReleased(Button button) {
    this.theme.mouseReleased(button);
  }
}

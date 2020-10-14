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

import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.api.IInputReceiver;
import org.sorus.client.gui.core.component.api.IRenderable;
import org.sorus.client.gui.core.component.api.IScreenRenderer;

/**
 * Core class for use in the {@link GUIManager} for rendering to the screen. Can be displayed and
 * removed from display.
 */
public abstract class Screen implements IInputReceiver, IRenderable, IContainer {

  protected final IScreenRenderer renderer;

  public Screen() {
    this.renderer = Sorus.getSorus().getGUIManager().getRenderer();
  }

  /** Called when the screen if first opened. */
  public void onOpen() {}

  /** Called when the screen is closed. */
  public void onExit() {}

  /**
   * Used to determine if a blank gui needs to be displayed while this is a current screen.
   *
   * @return if the screen should take the user out of their game
   */
  public boolean shouldTakeOutOfGame() {
    return false;
  }

  @Override
  public boolean isInteractContainer() {
    List<Screen> currentScreens = Sorus.getSorus().getGUIManager().getCurrentScreens();
    return currentScreens.indexOf(this) == currentScreens.size() - 1;
  }
}

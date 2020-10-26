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

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.event.impl.client.render.Render2DEvent;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

/**
 * Manager for Sorus' custom gui system. Helps with displaying screens at certain times and calling
 * them for rendering. Instance stored in {@link Sorus}
 */
public class GUIManager {

  /** The currently displayed screens. */
  private final List<Screen> currentScreens = new ArrayList<>();

  /** The renderer implementation used for rendering to the screen. */
  private RendererImpl renderer;

  /** Stores whether a blank gui is opened. */
  private boolean guiOpen = false;

  private ThemeManager themeManager;

  /** Performs initialization duties to make sure it operates correctly. */
  public void initialize(ThemeManager themeManager) {
    this.themeManager = themeManager;
    Sorus.getSorus().getEventManager().register(this);
    this.renderer = new RendererImpl();
  }

  /**
   * Adds a screen to the current list of screens, for it to be displayed in the next frame.
   *
   * @param screen the screen being opened
   */
  public void open(Screen screen) {
    this.currentScreens.add(screen);
    screen.onOpen();
  }

  /**
   * Removes a screen from the list of screens, so it will quit rendering and be closed.
   *
   * @param screen the screen being closed
   */
  public void close(Screen screen) {
    this.currentScreens.remove(screen);
    screen.onExit();
  }

  /**
   * Renders all the current screens and opens or closes the blank gui depending on if it should be
   * open.
   *
   * @param e the {@link Render2DEvent}.
   */
  @EventInvoked
  public void onRender(Render2DEvent e) {
    boolean shouldGuiOpen = false;
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    for (Screen screen : currentScreens) {
      screen.onRender();
      if (screen.shouldTakeOutOfGame()) {
        shouldGuiOpen = true;
      }
    }
    if (shouldGuiOpen != guiOpen) {
      IGame game = Sorus.getSorus().getVersion().getGame();
      if (shouldGuiOpen) {
        game.displayBlankGUI();
      } else {
        game.removeBlankGUI();
      }
      guiOpen = shouldGuiOpen;
    }
  }

  /**
   * Returns if a screen of a certain class is currently being displayed.
   *
   * @param screenClass the class to check if a screen object of the class is being displayed
   * @return if a screen of that class is being displayed
   */
  public boolean isScreenOpen(Class<? extends Screen> screenClass) {
    for (Screen screen : this.currentScreens) {
      if (screen.getClass().equals(screenClass)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Calls the {@link Screen#keyTyped(Key)} method for all currently displayed screens.
   *
   * @param e the {@link KeyPressEvent}.
   */
  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    currentScreens.forEach(screen -> screen.keyTyped(e.getKey(), e.isRepeat()));
  }

  /**
   * Calls the {@link Screen#mouseClicked(Button, double, double)} method for all currently
   * displayed screens.
   *
   * @param e the {@link KeyPressEvent}.
   */
  @EventInvoked
  public void onMousePress(MousePressEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    currentScreens.forEach(screen -> screen.mouseClicked(e.getButton(), e.getX(), e.getY()));
  }

  /**
   * Calls the {@link Screen#mouseReleased(Button)} method for all currently displayed screens.
   *
   * @param e the {@link KeyPressEvent}.
   */
  @EventInvoked
  public void onMouseRelease(MouseReleaseEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    currentScreens.forEach(screen -> screen.mouseReleased(e.getButton()));
  }

  public RendererImpl getRenderer() {
    return renderer;
  }

  public List<Screen> getCurrentScreens() {
    return currentScreens;
  }
}

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

import java.util.Map;
import org.sorus.client.settings.ISettingHolder;

/** Base interface for all components, all components will implement this in some way. */
public interface IComponent extends ISettingHolder {

  /**
   * Renders the component.
   *
   * @param x x position to render
   * @param y y position to render
   */
  void render(double x, double y);

  default void onRemove() {}

  /**
   * Gets the width of the component.
   *
   * @return the component width
   */
  double getWidth();

  /**
   * Gets the height of the component.
   *
   * @return the component height
   */
  double getHeight();

  String getName();

  /**
   * Sets the hud, so the component knows where to render.
   *
   * @param hud the hud to set it to
   */
  void setHUD(HUD hud);

  default void onAdd() {

  }

  @Override
  Map<String, Object> getSettings();
}

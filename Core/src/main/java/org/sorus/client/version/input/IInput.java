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

package org.sorus.client.version.input;

public interface IInput {

  /**
   * Gets the current x position of the mouse cursor.
   *
   * @return the mouse x
   */
  double getMouseX();

  /**
   * Gets the current y position of the mouse cursor.
   *
   * @return the mouse y
   */
  double getMouseY();

  /**
   * Gets if a mouse button is pressed.
   *
   * @param button mouse button to check for
   * @return if the button is pressed
   */
  boolean isButtonDown(Button button);

  /**
   * Gets if a key is pressed.
   *
   * @param key key to check for
   * @return if the key is pressed
   */
  boolean isKeyDown(Key key);

  /**
   * Gets the current mouse scroll wheel scroll value.
   *
   * @return the scroll wheel scroll
   */
  double getScroll();

  IKeybind getKeybind(KeybindType keybind);
}

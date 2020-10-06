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

import org.sorus.client.Sorus;

/**
 * Key enum which makes it possible to reference a key and have it operate across all minecraft
 * versions.
 */
public enum Key implements Input {
  ESCAPE,
  F1,
  F2,
  F3,
  F4,
  F5,
  F6,
  F7,
  F8,
  F9,
  F10,
  F11,
  F12,
  GRAVE,
  ONE,
  TWO,
  THREE,
  FOUR,
  FIVE,
  SIX,
  SEVEN,
  EIGHT,
  NINE,
  ZERO,
  MINUS,
  EQUALS,
  BACKSPACE,
  TAB,
  Q,
  W,
  E,
  R,
  T,
  Y,
  U,
  I,
  O,
  P,
  BRACKET_OPEN,
  BRACKET_CLOSE,
  SLASH_BACK,
  CAPSLOCK,
  A,
  S,
  D,
  F,
  G,
  H,
  J,
  K,
  L,
  SEMICOLON,
  APOSTROPHE,
  ENTER,
  SHIFT_LEFT,
  Z,
  X,
  C,
  V,
  B,
  N,
  M,
  COMMA,
  PERIOD,
  SLASH_FORWARD,
  SHIFT_RIGHT,
  CONTROL_LEFT,
  META_LEFT,
  ALT_LEFT,
  SPACE,
  ALT_RIGHT,
  META_RIGHT,
  MENU,
  CONTROL_RIGHT,
  NULL;

  @Override
  public boolean isDown() {
    return Sorus.getSorus().getVersion().getInput().isKeyDown(this);
  }

  @Override
  public String getName() {
    return this.name();
  }
}

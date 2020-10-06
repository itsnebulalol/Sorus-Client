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

package org.sorus.client.gui.core.component.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.font.IFontRenderer;

/** Implementation of {@link Component} for drawing text. */
public class Text extends Component {

  private IFontRenderer fontRenderer;
  private String text = "";
  private boolean shadow;

  /** Draws text using the specified font renderer to the screen. */
  @Override
  public void onRender() {
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    renderer.drawString(
        fontRenderer,
        text,
        this.absoluteX(),
        this.absoluteY(),
        this.absoluteXScale(),
        this.absoluteYScale(),
        shadow,
        this.absoluteColor());
  }

  /**
   * Sets the font renderer of the text.
   *
   * @param fontRenderer the wanted font renderer
   * @return the text component
   */
  public <T extends Text> T fontRenderer(IFontRenderer fontRenderer) {
    this.fontRenderer = fontRenderer;
    return this.cast();
  }

  public IFontRenderer fontRenderer() {
    return this.fontRenderer;
  }

  /**
   * Sets the text.
   *
   * @param text the wanted text
   * @return the text component
   */
  public <T extends Text> T text(String text) {
    this.text = text;
    return this.cast();
  }

  public <T extends Text> T shadow(boolean shadow) {
    this.shadow = shadow;
    return this.cast();
  }

  /**
   * Gets the width of the current text.
   *
   * @return the width of the text
   */
  public double width() {
    return fontRenderer.getStringWidth(text);
  }

  /**
   * Gets the height of the font renderer's font.
   *
   * @return the height of the font renderer
   */
  public double height() {
    return fontRenderer.getFontHeight();
  }

  public String text() {
    return this.text;
  }
}

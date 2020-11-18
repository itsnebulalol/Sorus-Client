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

package org.sorus.onesixteenfour;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.sorus.client.Sorus;
import org.sorus.client.version.render.ITTFFontRenderer;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class SlickFontRenderer extends UnicodeFont implements ITTFFontRenderer {

  public SlickFontRenderer(String fontPath)
      throws FontFormatException, IOException, SlickException {
    super(
        Font.createFont(
                Font.TRUETYPE_FONT, Objects.requireNonNull(SlickFontRenderer.class.getClassLoader().getResourceAsStream(fontPath)))
            .deriveFont(80f));
    this.addAsciiGlyphs();
    this.getEffects().add(new ColorEffect(Color.WHITE));
    this.loadGlyphs();
    this.setDisplayListCaching(false);
  }

  @Override
  public void drawString(
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color) {
    if(string.trim().isEmpty()) {
      return;
    }
    Sorus.getSorus().getVersion().getGLHelper().translate(x - 0.25, y - 2, 0);
    Sorus.getSorus().getVersion().getGLHelper().scale(1.0 / 9 * xScale, 1.0 / 9 * yScale, 1);
    Sorus.getSorus().getVersion().getGLHelper().blend(true);
    Sorus.getSorus()
        .getVersion()
        .getGLHelper()
        .color(
            color.getRed() / 255.0,
            color.getGreen() / 255.0,
            color.getBlue() / 255.0,
            color.getAlpha() / 255.0);
    org.newdawn.slick.Color color1 =
        new org.newdawn.slick.Color(
            color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    super.drawString(0, 0, string, color1);
    Sorus.getSorus().getVersion().getGLHelper().blend(false);
    Sorus.getSorus().getVersion().getGLHelper().scale(9 / xScale, 9 / yScale, 1);
    Sorus.getSorus().getVersion().getGLHelper().translate(-x + 0.25, -y + 2, 0);
    Sorus.getSorus().getVersion().getGLHelper().unbindTexture();
  }

  @Override
  public double getStringWidth(String string) {
    return super.getWidth(string) / 9.0;
  }

  @Override
  public double getFontHeight() {
    return super.getLineHeight() / 15.0;
  }

  @Override
  public int getSpacing() {
    return 0;
  }

}

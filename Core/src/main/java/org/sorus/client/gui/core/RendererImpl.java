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

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.core.font.MinecraftFontRenderer;
import org.sorus.client.version.render.IRenderer;

/**
 * Implementation of {@link IScreenRenderer} used for rendering to the screen. Instance stored in
 * {@link GUIManager}.
 */
public class RendererImpl implements IScreenRenderer {

  /** A wrapper for the default minecraft font renderer. */
  private final IFontRenderer MINECRAFT_FONT_RENDERER =
      new MinecraftFontRenderer("textures/font/ascii.png");

  /** Font renderer for the Abel font using slick2d. */
  private IFontRenderer abelFontRenderer;

  /** Font renderer for the Rubik font using slick2d. */
  private IFontRenderer rubikFontRenderer;

  /** Font renderer for the Godile font using slick2d. */
  private IFontRenderer gidoleFontRenderer;

  public RendererImpl() {
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void drawRect(
      double x,
      double y,
      double width,
      double height,
      Color bottomLeftColor,
      Color bottomRightColor,
      Color topRightColor,
      Color topLeftColor) {
    Sorus.getSorus()
        .getVersion()
        .getRenderer()
        .drawRect(
            x, y, width, height, bottomLeftColor, bottomRightColor, topRightColor, topLeftColor);
  }

  public void drawHollowRect(
      double x,
      double y,
      double width,
      double height,
      double thickness,
      double cornerRadius,
      Color color) {
    this.drawHollowRect(x, y, width, height, thickness, cornerRadius, cornerRadius, color);
  }

  @Override
  public void drawHollowRect(
      double x,
      double y,
      double width,
      double height,
      double thickness,
      double xCornerRadius,
      double yCornerRadius,
      Color color) {
    org.sorus.client.version.render.IRenderer renderer =
        Sorus.getSorus().getVersion().getRenderer();
    if (xCornerRadius > 0 && yCornerRadius > 0) {
      renderer.drawHollowArc(x, y, xCornerRadius, yCornerRadius, 180, 270, thickness, color);
      renderer.drawHollowArc(
          x + width - xCornerRadius * 2,
          y,
          xCornerRadius,
          yCornerRadius,
          90,
          180,
          thickness,
          color);
      renderer.drawHollowArc(
          x,
          y + height - yCornerRadius * 2,
          xCornerRadius,
          yCornerRadius,
          270,
          360,
          thickness,
          color);
      renderer.drawHollowArc(
          x + width - xCornerRadius * 2,
          y + height - yCornerRadius * 2,
          xCornerRadius,
          yCornerRadius,
          0,
          90,
          thickness,
          color);
    } else {
      xCornerRadius = thickness;
      yCornerRadius = thickness;
      renderer.drawRect(x, y, thickness, thickness, color, color, color, color);
      renderer.drawRect(x + width - thickness, y, thickness, thickness, color, color, color, color);
      renderer.drawRect(
          x, y + height - thickness, thickness, thickness, color, color, color, color);
      renderer.drawRect(
          x + width - thickness,
          y + height - thickness,
          thickness,
          thickness,
          color,
          color,
          color,
          color);
    }
    renderer.drawRect(
        x + xCornerRadius, y, width - xCornerRadius * 2, thickness, color, color, color, color);
    renderer.drawRect(
        x, y + yCornerRadius, thickness, height - yCornerRadius * 2, color, color, color, color);
    renderer.drawRect(
        x + width - thickness,
        y + yCornerRadius,
        thickness,
        height - yCornerRadius * 2,
        color,
        color,
        color,
        color);
    renderer.drawRect(
        x + xCornerRadius,
        y + height - thickness,
        width - xCornerRadius * 2,
        thickness,
        color,
        color,
        color,
        color);
  }

  @Override
  public void drawString(
      IFontRenderer fontRenderer,
      String string,
      double x,
      double y,
      double xScale,
      double yScale,
      boolean shadow,
      Color color) {
    fontRenderer.drawString(string, x, y, xScale, yScale, shadow, color);
  }

  public IFontRenderer getMinecraftFontRenderer() {
    return MINECRAFT_FONT_RENDERER;
  }

  public IFontRenderer getAbelFontRenderer() {
    return abelFontRenderer;
  }

  public IFontRenderer getRubikFontRenderer() {
    return rubikFontRenderer;
  }

  public IFontRenderer getGidoleFontRenderer() {
    return gidoleFontRenderer;
  }

  @Override
  public void drawArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      Color color) {
    Sorus.getSorus()
        .getVersion()
        .getRenderer()
        .drawArc(x, y, xRadius, yRadius, startAngle, endAngle, color);
  }

  @Override
  public void drawHollowArc(
      double x,
      double y,
      double xRadius,
      double yRadius,
      int startAngle,
      int endAngle,
      double thickness,
      Color color) {
    Sorus.getSorus()
        .getVersion()
        .getRenderer()
        .drawHollowArc(x, y, xRadius, yRadius, startAngle, endAngle, thickness, color);
  }

  @Override
  public void drawImage(
      String resource,
      double x,
      double y,
      double width,
      double height,
      double textureX,
      double textureY,
      double textureWidth,
      double textureHeight,
      Color color) {
    Sorus.getSorus()
        .getVersion()
        .getRenderer()
        .drawImage(
            resource, x, y, width, height, textureX, textureY, textureWidth, textureHeight, color);
  }

  @EventInvoked
  public void onStart(StartEvent e) {
    IRenderer renderer = Sorus.getSorus().getVersion().getRenderer();
    abelFontRenderer = renderer.getFont("Abel.ttf");
    rubikFontRenderer = renderer.getFont("Rubik.ttf");
    gidoleFontRenderer = renderer.getFont("Gidole.ttf");
  }
}

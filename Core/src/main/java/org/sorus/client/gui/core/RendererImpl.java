package org.sorus.client.gui.core;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.core.font.MinecraftFontRenderer;
import org.sorus.client.version.game.IItemStack;
import org.sorus.client.version.render.IRenderer;

public class RendererImpl implements IScreenRenderer {

  private IFontRenderer minecraftFontRenderer;
  private IFontRenderer abelFontRenderer;
  private IFontRenderer rubikFontRenderer;
  private IFontRenderer gidoleFontRenderer;
  private IFontRenderer rawLineFontRenderer;
  private IFontRenderer robotoFontRenderer;

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
        .getData(IRenderer.class)
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
    IRenderer renderer = Sorus.getSorus().getVersion().getData(IRenderer.class);
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
        .getData(IRenderer.class)
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
        .getData(IRenderer.class)
        .drawHollowArc(x, y, xRadius, yRadius, startAngle, endAngle, thickness, color);
  }

  @Override
  public void drawPartialImage(
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
        .getData(IRenderer.class)
        .drawPartialImage(
            resource, x, y, width, height, textureX, textureY, textureWidth, textureHeight, color);
  }

  @Override
  public void drawFullImage(
      String resource, double x, double y, double width, double height, Color color) {
    Sorus.getSorus()
        .getVersion()
        .getData(IRenderer.class)
        .drawFullImage(resource, x, y, width, height, color);
  }

  @Override
  public void drawItem(IItemStack itemStack, double x, double y, Color color) {
    Sorus.getSorus().getVersion().getData(IRenderer.class).drawItem(itemStack, x, y, color);
  }

  @EventInvoked
  public void onStart(StartEvent e) {
    IRenderer renderer = Sorus.getSorus().getVersion().getData(IRenderer.class);
    abelFontRenderer = renderer.getFont("Abel.ttf");
    rubikFontRenderer = renderer.getFont("Rubik.ttf");
    gidoleFontRenderer = renderer.getFont("Gidole.ttf");
    rawLineFontRenderer = renderer.getFont("Rawline.ttf");
    robotoFontRenderer = renderer.getFont("Roboto.ttf");
    minecraftFontRenderer = new MinecraftFontRenderer("textures/font/ascii.png");
  }

  public IFontRenderer getMinecraftFontRenderer() {
    return minecraftFontRenderer;
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

  public IFontRenderer getRawLineFontRenderer() {
    return rawLineFontRenderer;
  }

  public IFontRenderer getRobotoFontRenderer() {
    return robotoFontRenderer;
  }
}

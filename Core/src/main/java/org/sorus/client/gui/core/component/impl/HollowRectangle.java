package org.sorus.client.gui.core.component.impl;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.api.IScreenRenderer;

public class HollowRectangle extends Rectangle {

  private double thickness = 1;

  @Override
  public void onRender() {
    double x = this.absoluteX();
    double y = this.absoluteY();
    double width = this.width * this.absoluteXScale();
    double height = this.height * this.absoluteYScale();
    double thickness = this.thickness * this.absoluteXScale();
    double xCornerRadius = this.cornerRadius * this.absoluteXScale();
    double yCornerRadius = this.cornerRadius * this.absoluteYScale();
    Color color = this.absoluteColor();
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    renderer.drawHollowRect(x, y, width, height, thickness, xCornerRadius, yCornerRadius, color);
  }

  public <T extends HollowRectangle> T thickness(double thickness) {
    this.thickness = thickness;
    return this.cast();
  }
}

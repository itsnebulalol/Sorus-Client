package org.sorus.client.gui.core.component.impl;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.api.IScreenRenderer;

public class Image extends Component {

  private String resource;
  private double width, height;
  private double textureX, textureY;
  private double textureWidth, textureHeight;
  private boolean fullImage = true;

  @Override
  public void onRender() {
    double x = this.absoluteX();
    double y = this.absoluteY();
    double width = this.width * this.absoluteXScale();
    double height = this.height * this.absoluteYScale();
    Color color = this.absoluteColor();
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    if (this.fullImage) {
      renderer.drawFullImage(resource, x, y, width, height, color);
    } else {
      renderer.drawPartialImage(
          resource, x, y, width, height, textureX, textureY, textureWidth, textureHeight, color);
    }
  }

  public <T extends Image> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }

  public <T extends Image> T resource(String resource) {
    this.resource = resource;
    return this.cast();
  }

  public <T extends Image> T texturePosition(double textureX, double textureY) {
    this.textureX = textureX;
    this.textureY = textureY;
    this.fullImage = false;
    return this.cast();
  }

  public <T extends Image> T textureSize(double textureWidth, double textureHeight) {
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.fullImage = false;
    return this.cast();
  }
}

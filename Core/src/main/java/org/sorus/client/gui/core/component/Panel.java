package org.sorus.client.gui.core.component;

import java.awt.*;
import org.sorus.client.gui.core.IContainer;

public class Panel extends Collection {

  private final IContainer container;

  public Panel(IContainer container) {
    this.container = container;
  }

  public Panel() {
    this(null);
  }

  @Override
  public double absoluteX() {
    return this.x;
  }

  @Override
  public double absoluteY() {
    return this.y;
  }

  @Override
  public double absoluteXScale() {
    return this.xScale;
  }

  @Override
  public double absoluteYScale() {
    return this.yScale;
  }

  @Override
  public Color absoluteColor() {
    return this.color;
  }

  @Override
  public IContainer getContainer() {
    return container;
  }
}

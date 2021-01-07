package org.sorus.client.gui.core.modifier.impl;

public class Expand extends Hover {

  private final double maxPercent;

  public Expand(double time, double width, double height, double maxPercent) {
    super(time, width, height);
    this.maxPercent = maxPercent;
  }

  @Override
  protected void onUpdated() {
    component
        .position(-percent * maxPercent * width / 2, -percent * maxPercent * height / 2)
        .scale(1 + percent * maxPercent, 1 + percent * maxPercent);
  }
}

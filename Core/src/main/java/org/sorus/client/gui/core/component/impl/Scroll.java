package org.sorus.client.gui.core.component.impl;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.util.MathUtil;

public class Scroll extends Collection {

  private double scroll;
  private double minScroll, maxScroll;

  @Override
  public double absoluteY() {
    return super.absoluteY() + this.getScroll() * this.absoluteYScale();
  }

  public double setScroll(double scroll) {
    this.scroll = MathUtil.clamp(scroll, this.getMinScroll(), this.getMaxScroll());
    return this.scroll;
  }

  public double getScroll() {
    return scroll;
  }

  public double getMinScroll() {
    return minScroll;
  }

  public double getMaxScroll() {
    return maxScroll;
  }

  public void addMinMaxScroll(double minScroll, double maxScroll) {
    this.minScroll = minScroll;
    this.maxScroll = maxScroll;
  }
}

package org.sorus.client.gui.core.component.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;

public class Arc extends Component {

  private double xRadius, yRadius;
  private int startAngle, endAngle;

  @Override
  public void onRender() {
    Sorus.getSorus()
        .getGUIManager()
        .getRenderer()
        .drawArc(
            this.absoluteX(),
            this.absoluteY(),
            this.xRadius * this.absoluteXScale(),
            this.yRadius * this.absoluteYScale(),
            startAngle,
            endAngle,
            this.absoluteColor());
  }

  public <T extends Arc> T radius(double xRadius, double yRadius) {
    this.xRadius = xRadius;
    this.yRadius = yRadius;
    return this.cast();
  }

  public <T extends Arc> T angle(int startAngle, int endAngle) {
    this.startAngle = startAngle;
    this.endAngle = endAngle;
    return this.cast();
  }
}

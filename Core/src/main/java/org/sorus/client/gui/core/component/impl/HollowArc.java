package org.sorus.client.gui.core.component.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;

public class HollowArc extends Component {

  private double xRadius, yRadius;
  private int startAngle, endAngle;
  private double thickness;

  @Override
  public void onRender() {
    Sorus.getSorus()
        .getGUIManager()
        .getRenderer()
        .drawHollowArc(
            this.absoluteX(),
            this.absoluteY(),
            this.xRadius * this.absoluteXScale(),
            this.yRadius * this.absoluteYScale(),
            startAngle,
            endAngle,
            thickness * this.absoluteXScale(),
            this.absoluteColor());
  }

  public <T extends HollowArc> T radius(double xRadius, double yRadius) {
    this.xRadius = xRadius;
    this.yRadius = yRadius;
    return this.cast();
  }

  public <T extends HollowArc> T angle(int startAngle, int endAngle) {
    this.startAngle = startAngle;
    this.endAngle = endAngle;
    return this.cast();
  }

  public <T extends HollowArc> T thickness(double thickness) {
    this.thickness = thickness;
    return this.cast();
  }
}

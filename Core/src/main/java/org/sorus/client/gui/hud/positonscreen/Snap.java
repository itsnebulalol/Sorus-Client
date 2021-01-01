package org.sorus.client.gui.hud.positonscreen;

import org.sorus.client.util.Axis;

public class Snap {

  private final double value, distance, offset;
  private final boolean snapped;
  private final Axis axis;

  public Snap(double value, double distance, double offset, boolean snapped, Axis axis) {
    this.value = value;
    this.distance = distance;
    this.offset = offset;
    this.snapped = snapped;
    this.axis = axis;
  }

  public double getValue() {
    return value;
  }

  public double getDistance() {
    return distance;
  }

  public double getOffset() {
    return offset;
  }

  public boolean isSnapped() {
    return snapped;
  }

  public Axis getAxis() {
    return axis;
  }
}

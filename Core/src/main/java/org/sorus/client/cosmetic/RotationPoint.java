package org.sorus.client.cosmetic;

public class RotationPoint {

  private final double x, y, z;
  private double xRot, yRot, zRot;

  public RotationPoint(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void updateRotation(double x, double y, double z) {
    this.xRot = x;
    this.yRot = y;
    this.zRot = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public double getXRot() {
    return xRot;
  }

  public double getYRot() {
    return yRot;
  }

  public double getZRot() {
    return zRot;
  }
}

package org.sorus.client.gui.hud;

public class HUDPosition {

  private double x, y;
  private double offsetX, offsetY;

  public HUDPosition() {
    this.x = 0.5;
    this.y = 0.5;
  }

  public void setX(HUD hud, double x, double screenWidth) {
    double hudWidth = hud.getWidth() * hud.getScale();
    double leftDistance = Math.abs(0 - (x - hudWidth / 2));
    double centerDistance = Math.abs(screenWidth / 2 - x);
    double rightDistance = Math.abs(screenWidth - (x + hudWidth / 2));
    if (leftDistance < centerDistance && leftDistance < rightDistance) {
      this.offsetX = -0.5;
    }
    if (centerDistance < leftDistance && centerDistance < rightDistance) {
      this.offsetX = 0;
    }
    if (rightDistance < leftDistance && rightDistance < centerDistance) {
      this.offsetX = 0.5;
    }
    this.x = (x + hudWidth * offsetX) / screenWidth;
  }

  public void setY(HUD hud, double y, double screenHeight) {
    double hudHeight = hud.getHeight() * hud.getScale();
    double topDistance = Math.abs(0 - (y - hudHeight / 2));
    double centerDistance = Math.abs(screenHeight / 2 - y);
    double bottomDistance = Math.abs(screenHeight - (y + hudHeight / 2));
    if (topDistance < centerDistance && topDistance < bottomDistance) {
      this.offsetY = -0.5;
    }
    if (centerDistance < topDistance && centerDistance < bottomDistance) {
      this.offsetY = 0;
    }
    if (bottomDistance < topDistance && bottomDistance < centerDistance) {
      this.offsetY = 0.5;
    }
    this.y = (y + hudHeight * offsetY) / screenHeight;
  }

  public double getX(HUD hud, double screenWidth) {
    return (this.x - (this.offsetX * hud.getWidth() * hud.getScale()) / screenWidth) * screenWidth;
  }

  public double getY(HUD hud, double screenHeight) {
    return (this.y - (this.offsetY * hud.getHeight() * hud.getScale()) / screenHeight)
        * screenHeight;
  }
}

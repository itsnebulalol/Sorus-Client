package org.sorus.client.gui.hud.positonscreen;

public class SelectedHUDState {

  private final InteractType interactType;
  private final double initialX;
  private final double initialY;
  private final double initialScale;
  private final double initialMouseX;
  private final double initialMouseY;

  public SelectedHUDState(
      InteractType interactType,
      double initialX,
      double initialY,
      double initialScale,
      double initialMouseX,
      double initialMouseY) {
    this.interactType = interactType;
    this.initialX = initialX;
    this.initialY = initialY;
    this.initialScale = initialScale;
    this.initialMouseX = initialMouseX;
    this.initialMouseY = initialMouseY;
  }

  public InteractType getInteractType() {
    return interactType;
  }

  public double getInitialX() {
    return initialX;
  }

  public double getInitialY() {
    return initialY;
  }

  public double getInitialMouseX() {
    return initialMouseX;
  }

  public double getInitialMouseY() {
    return initialMouseY;
  }

  public double getInitialScale() {
    return initialScale;
  }

  public enum InteractType {
    DRAG,
    RESIZE_LEFT_TOP,
    RESIZE_LEFT_BOTTOM,
    RESIZE_RIGHT_TOP,
    RESIZE_RIGHT_BOTTOM
  }
}

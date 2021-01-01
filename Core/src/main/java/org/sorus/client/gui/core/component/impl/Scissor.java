package org.sorus.client.gui.core.component.impl;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.version.IGLHelper;

public class Scissor extends Collection {

  private static final List<ScissorFrame> currentScissors = new ArrayList<>();

  public static void addScissor(double x, double y, double width, double height) {
    currentScissors.add(new ScissorFrame(x, y, width, height));
    updateCurrentScissor();
  }

  public static void removeScissor() {
    currentScissors.remove(currentScissors.size() - 1);
    updateCurrentScissor();
  }

  private static void updateCurrentScissor() {
    if (currentScissors.size() == 0) {
      Sorus.getSorus().getVersion().getData(IGLHelper.class).endScissor();
      return;
    }
    double left = -Double.MAX_VALUE;
    double top = -Double.MAX_VALUE;
    double right = Double.MAX_VALUE;
    double bottom = Double.MAX_VALUE;
    for (ScissorFrame scissor : currentScissors) {
      left = Math.max(scissor.getX(), left);
      top = Math.max(scissor.getY(), top);
      right = Math.min(scissor.getX() + scissor.getWidth(), right);
      bottom = Math.min(scissor.getY() + scissor.getHeight(), bottom);
    }
    if (left > right || top > bottom) {
      IGLHelper iglHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
      iglHelper.beginScissor(0, 0, 0, 0);
      return;
    }
    Sorus.getSorus()
        .getVersion()
        .getData(IGLHelper.class)
        .beginScissor(left, top, right - left, bottom - top);
  }

  private static class ScissorFrame {

    private final double x, y;
    private final double width, height;

    public ScissorFrame(double x, double y, double width, double height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }

    public double getWidth() {
      return width;
    }

    public double getHeight() {
      return height;
    }
  }

  private double width, height;

  @Override
  public void onRender() {
    Scissor.addScissor(
        this.absoluteX(),
        this.absoluteY(),
        this.width * this.absoluteXScale(),
        this.height * this.absoluteYScale());
    super.onRender();
    Scissor.removeScissor();
  }

  public <T extends Scissor> T size(double width, double height) {
    this.width = width;
    this.height = height;
    return this.cast();
  }
}

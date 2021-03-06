package org.sorus.client.module.impl.music.screen;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Arc;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class MusicSlider extends Collection {

  private final double minValue, maxValue;

  private boolean selected;

  private double value;

  private final Arc selector;
  private final Rectangle selector2;

  private double prevMouseX;
  private boolean editing;

  public MusicSlider() {
    this.minValue = 0;
    this.maxValue = 1;
    this.add(
        new Rectangle().smooth(3.5).size(250, 7).position(0, 6.5).color(new Color(80, 80, 80)));
    this.add(
        this.selector2 =
            new Rectangle().smooth(3.5).position(0, 6.5).color(new Color(210, 210, 210)));
    this.add(this.selector = new Arc().angle(0, 360).radius(7, 7).color(new Color(210, 210, 210)));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    if (this.selected) {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      editing = mouseX != prevMouseX;
      if (this.editing) {
        double baseValue =
            (MathUtil.clamp(
                        mouseX, this.absoluteX(), this.absoluteX() + 250 * this.absoluteXScale())
                    - (this.absoluteX()))
                / (250 * this.absoluteXScale());
        value = minValue + (baseValue * (maxValue - minValue));
      }
      prevMouseX = mouseX;
    } else {
      editing = false;
    }
    this.selector.position(((value - minValue) / (maxValue - minValue)) * 250 - 7, 3);
    this.selector2.size(((value - minValue) / (maxValue - minValue)) * 250, 7);
    super.onRender();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    selected = this.isHovered(e.getX(), e.getY());
    this.onRender();
  }

  @EventInvoked
  public void onRelease(MouseReleaseEvent e) {
    selected = false;
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + 250 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 40 * this.absoluteYScale();
  }

  public void setValue(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public boolean isSelected() {
    return selected;
  }

  public boolean isEditing() {
    return editing;
  }
}

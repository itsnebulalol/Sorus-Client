package org.sorus.client.gui.core.component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.gui.core.IContainer;
import org.sorus.client.gui.core.component.api.IRenderable;
import org.sorus.client.gui.core.modifier.Modifier;
import org.sorus.client.gui.core.util.ColorUtil;

public abstract class Component implements IRenderable {

  protected double x, y;
  protected double xScale = 1, yScale = 1;
  protected Color color = Color.WHITE;

  protected Collection parent;

  protected List<Modifier<?>> modifiers = new ArrayList<>();

  public void onUpdate() {
    for (Modifier<?> modifier : this.modifiers) {
      modifier.onUpdate();
    }
  }

  public void onRemove() {
    for (Modifier<?> modifier : this.modifiers) {
      modifier.onRemove();
    }
  }

  public <T extends Component> T position(double x, double y) {
    this.x = x;
    this.y = y;
    return this.cast();
  }

  public <T extends Component> T scale(double xScale, double yScale) {
    this.xScale = xScale;
    this.yScale = yScale;
    return this.cast();
  }

  public <T extends Component> T color(Color color) {
    this.color = color;
    return this.cast();
  }

  public <T extends Component> T attach(Modifier<Component> modifier) {
    this.modifiers.add(modifier);
    modifier.setComponent(this);
    return this.cast();
  }

  public double rawX() {
    return x;
  }

  public double rawY() {
    return y;
  }

  public double absoluteX() {
    return this.parent.absoluteX() + x * this.parent.absoluteXScale();
  }

  public double absoluteY() {
    return this.parent.absoluteY() + y * this.parent.absoluteYScale();
  }

  public double absoluteXScale() {
    return this.parent.absoluteXScale() * xScale;
  }

  public double absoluteYScale() {
    return this.parent.absoluteYScale() * yScale;
  }

  public Color absoluteColor() {
    return ColorUtil.average(this.parent.absoluteColor(), color);
  }

  protected void setParent(Collection parent) {
    this.parent = parent;
  }

  public Collection getParent() {
    return parent;
  }

  public IContainer getContainer() {
    return this.getParent().getContainer();
  }

  public List<Modifier<?>> getModifiers() {
    return modifiers;
  }

  protected <T extends Component> T cast() {
    return (T) this;
  }
}

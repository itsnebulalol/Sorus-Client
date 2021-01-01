package org.sorus.client.gui.theme;

public class ThemeBase<T> implements ITheme<T> {

  private T parent;

  @Override
  public void setParent(T screen) {
    this.parent = screen;
  }

  public T getParent() {
    return parent;
  }
}

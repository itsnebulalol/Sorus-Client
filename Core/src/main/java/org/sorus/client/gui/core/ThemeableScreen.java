package org.sorus.client.gui.core;

import org.sorus.client.gui.theme.ITheme;
import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

public class ThemeableScreen extends Screen {

  private final ITheme<?> theme;

  public ThemeableScreen(ITheme<ThemeableScreen> theme) {
    this.theme = theme;
    theme.setParent(this);
  }

  @Override
  public void onOpen() {
    this.theme.init();
  }

  @Override
  public void onUpdate() {
    this.theme.update();
  }

  @Override
  public void onRender() {
    this.theme.render();
  }

  @Override
  public void onExit() {
    this.theme.exit();
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    this.theme.onKeyType(key, repeat);
  }

  @Override
  public void onMouseClick(Button button, double x, double y) {
    this.theme.onMouseClick(button, x, y);
  }

  @Override
  public void onMouseRelease(Button button) {
    this.theme.onMouseRelease(button);
  }
}

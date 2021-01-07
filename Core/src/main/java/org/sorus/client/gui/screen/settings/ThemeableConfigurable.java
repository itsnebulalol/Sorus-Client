package org.sorus.client.gui.screen.settings;

import org.sorus.client.gui.theme.ThemeBase;

public abstract class ThemeableConfigurable extends Configurable {

  private final ThemeBase<?> theme;

  public ThemeableConfigurable(ThemeBase<ThemeableConfigurable> theme) {
    this.theme = theme;
    theme.setParent(this);
    theme.init();
  }

  @Override
  public void onUpdate() {
    theme.update();
  }

  @Override
  public void onRender() {
    theme.render();
  }

  @Override
  public void onRemove() {
    theme.exit();
  }

  @Override
  public double getHeight() {
    return ((IConfigurableTheme) this.getTheme()).getHeight();
  }

  public ThemeBase<?> getTheme() {
    return theme;
  }
}

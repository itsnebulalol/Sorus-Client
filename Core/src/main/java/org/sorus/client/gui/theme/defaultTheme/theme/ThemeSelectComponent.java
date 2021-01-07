package org.sorus.client.gui.theme.defaultTheme.theme;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.theme.Theme;

public class ThemeSelectComponent extends Collection {

  public static final double WIDTH = 835, HEIGHT = 70;

  private final DefaultSelectThemeScreen theme;
  private final Theme themeToSelect;

  private final HollowRectangle hollowRectangle;

  public ThemeSelectComponent(DefaultSelectThemeScreen theme, Theme themeToSelect) {
    this.theme = theme;
    this.themeToSelect = themeToSelect;
    final double ROUNDING = 10;
    this.add(
        new Rectangle()
            .size(WIDTH, HEIGHT)
            .smooth(ROUNDING)
            .color(theme.getDefaultTheme().getForegroundColorNew()));
    this.add(
        hollowRectangle = new HollowRectangle().thickness(2).size(WIDTH, HEIGHT).smooth(ROUNDING));
    this.deselect();
    this.add(
        new Image()
            .resource("sorus/modules/test_icon.png")
            .size(45, 45)
            .position(12.5, 12.5)
            .color(theme.getDefaultTheme().getElementColorNew()));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
            .text(themeToSelect.getName())
            .scale(3, 3)
            .position(80, 27.5)
            .color(theme.getDefaultTheme().getElementColorNew()));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
    super.onRemove();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (e.getX() > this.absoluteX()
        && e.getX() < this.absoluteX() + 680 * this.absoluteXScale()
        && e.getY() > this.absoluteY()
        && e.getY() < this.absoluteY() + 125 * this.absoluteYScale()) {
      this.select();
    }
  }

  public void select() {
    if (this.theme.getSelected() != null) {
      this.theme.getSelected().deselect();
    }
    this.theme.setSelected(this);
    this.hollowRectangle.color(this.theme.getDefaultTheme().getElementSecondColorNew());
  }

  public void deselect() {
    this.hollowRectangle.color(this.theme.getDefaultTheme().getElementMedgroundColorNew());
  }

  public Theme getTheme() {
    return themeToSelect;
  }
}

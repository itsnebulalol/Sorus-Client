package org.sorus.client.gui.theme.defaultTheme.menu;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;

public class MenuComponent extends Collection {

  public static final double WIDTH = 193, HEIGHT = 193;

  private final Runnable runnable;

  private double hoveredPercent;

  public MenuComponent(String name, ILogoCreator logoCreator, Runnable runnable) {
    this.runnable = runnable;
    final double ROUNDING = 10;
    this.add(
        new Rectangle()
            .size(WIDTH, HEIGHT)
            .smooth(ROUNDING)
            .color(DefaultTheme.getForegroundColorNew()));
    this.add(
        new HollowRectangle()
            .thickness(3)
            .size(WIDTH, HEIGHT)
            .smooth(ROUNDING)
            .color(DefaultTheme.getElementMedgroundColorNew()));
    Text text =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text(name)
            .scale(3, 3);
    this.add(text.position(WIDTH / 2 - text.width() * 3 / 2, HEIGHT - text.height() * 3 * 1.75));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      runnable.run();
    }
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + MenuComponent.WIDTH * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + MenuComponent.HEIGHT * this.absoluteYScale();
  }

  public interface ILogoCreator {
    void addLogoComponents();
  }
}

package org.sorus.client.gui.theme;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class ExitButton extends Collection {

  private final Runnable runnable;
  private final Collection main;

  private double hoverPercent;

  public ExitButton(Runnable runnable) {
    this.runnable = runnable;
    this.add(main = new Collection());
    main.add(
        new Image()
            .resource("sorus/back_arrow.png")
            .size(50, 50)
            .color(DefaultTheme.getElementColorNew()));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
    double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
    boolean hovered = this.isHovered(mouseX, mouseY);
    hoverPercent = MathUtil.clamp(hoverPercent + (hovered ? 1 : -1) * 0.1, 0, 1);
    main.position(-1 * hoverPercent, -1 * hoverPercent)
        .scale(1 + hoverPercent * 0.04, 1 + hoverPercent * 0.04);
    super.onRender();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      runnable.run();
    }
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + 50 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 50 * this.absoluteYScale();
  }
}

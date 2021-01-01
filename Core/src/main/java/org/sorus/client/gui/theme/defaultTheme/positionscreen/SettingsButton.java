package org.sorus.client.gui.theme.defaultTheme.positionscreen;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class SettingsButton extends Collection {

  private final Runnable runnable;
  private final Collection main;

  private long prevRenderTime;
  private double expandedPercent;

  public SettingsButton(Runnable runnable) {
    this.runnable = runnable;
    this.add(main = new Collection());
    main.add(new Rectangle().smooth(5).size(50, 50).color(DefaultTheme.getMedgroundLayerColor()));
    main.add(
        new Image()
            .resource("sorus/gear.png")
            .size(40, 40)
            .position(5, 5)
            .color(DefaultTheme.getForegroundLayerColor()));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    long renderTime = System.currentTimeMillis();
    long deltaTime = renderTime - prevRenderTime;
    boolean hovered =
        this.isHovered(
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
            Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
    expandedPercent = MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
    main.scale(1 + 0.05 * expandedPercent, 1 + 0.05 * expandedPercent)
        .position(-0.02 * 50 * expandedPercent, -0.02 * 50 * expandedPercent);
    prevRenderTime = renderTime;
    super.onRender();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
    super.onRemove();
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

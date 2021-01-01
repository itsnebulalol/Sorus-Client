package org.sorus.client.module.impl.crosshair;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.Configurable;

public class ViewCrosshair extends Configurable {

  public ViewCrosshair() {
    this.add(
        new Image()
            .resource("sorus/modules/crosshair/background.png")
            .size(200, 200)
            .position(20, 0));
  }

  @Override
  public void onRender() {
    super.onRender();
    Sorus.getSorus()
        .getModuleManager()
        .getModule(Crosshair.class)
        .renderCrosshair(
            this.absoluteX() + 120 * this.absoluteXScale(),
            this.absoluteY() + 100 * this.absoluteYScale());
  }

  @Override
  public double getHeight() {
    return 200;
  }
}

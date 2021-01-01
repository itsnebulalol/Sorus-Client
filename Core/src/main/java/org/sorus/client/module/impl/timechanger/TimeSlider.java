package org.sorus.client.module.impl.timechanger;

import java.awt.*;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.settings.Setting;

public class TimeSlider extends Slider {

  public TimeSlider(Setting<Double> setting, double minValue, double maxValue, String description) {
    super(setting, minValue, maxValue, description);
    this.add(
        new Image()
            .resource("sorus/modules/timechanger/sun.png")
            .size(25, 25)
            .position(460, 55)
            .color(new Color(235, 235, 235)));
    this.add(
        new Image()
            .resource("sorus/modules/timechanger/moon.png")
            .size(25, 25)
            .position(640, 55)
            .color(new Color(235, 235, 235)));
  }

  @Override
  public double getHeight() {
    return 110;
  }
}

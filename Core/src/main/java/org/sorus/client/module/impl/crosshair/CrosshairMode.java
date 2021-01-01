package org.sorus.client.module.impl.crosshair;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;

public abstract class CrosshairMode extends Mode {

  public abstract void render(double xCenter, double yCenter);

  public static class SplitLinesMode extends CrosshairMode {

    private final Setting<Double> lineLength;
    private final Setting<Double> centerGap;
    private final Setting<Double> thickness;
    private final Setting<Color> color;

    public SplitLinesMode() {
      this.register(lineLength = new Setting<>("lineLength", 5.));
      this.register(centerGap = new Setting<>("centerGap", 5.));
      this.register(thickness = new Setting<>("thickness", 1.));
      this.register(color = new Setting<>("color", Color.WHITE));
    }

    @Override
    public void render(double xCenter, double yCenter) {
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .drawRect(
              xCenter - thickness.getValue() / 2,
              yCenter - lineLength.getValue() - centerGap.getValue(),
              thickness.getValue(),
              lineLength.getValue(),
              color.getValue());
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .drawRect(
              xCenter - thickness.getValue() / 2,
              yCenter + centerGap.getValue(),
              thickness.getValue(),
              lineLength.getValue(),
              color.getValue());
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .drawRect(
              xCenter - lineLength.getValue() - centerGap.getValue(),
              yCenter - thickness.getValue() / 2,
              lineLength.getValue(),
              thickness.getValue(),
              color.getValue());
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .drawRect(
              xCenter + centerGap.getValue(),
              yCenter - thickness.getValue() / 2,
              lineLength.getValue(),
              thickness.getValue(),
              color.getValue());
    }

    @Override
    public String getName() {
      return "Split Lines";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new Slider(lineLength, 1, 12, "Width"));
      collection.add(new Slider(centerGap, 1, 6, "Gap"));
      collection.add(new Slider(thickness, 1, 6, "Thickness"));
      collection.add(new ColorPicker(color, "Color"));
    }
  }
}

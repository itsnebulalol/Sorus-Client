package org.sorus.client.module.impl.gps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.CustomTextColor;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;

public abstract class GPSMode extends Mode {

  public abstract List<List<Pair<String, Color>>> format(int x, int y, int z);

  public abstract double getWidth(String[] fpsString, IFontRenderer fontRenderer);

  public abstract double getHeight(String[] fpsString, IFontRenderer fontRenderer);

  public static class LabelPreMode extends GPSMode {

    private final Setting<String> preLabel;
    private final Setting<String> postLabel;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> labelExtraColor;
    private final Setting<Color> valueColor;
    private final Setting<Boolean> tightFit;

    public LabelPreMode() {
      this.register(preLabel = new Setting<>("preLabel", "["));
      this.register(postLabel = new Setting<>("postLabel", "]"));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(labelExtraColor = new Setting<>("labelExtraColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
      this.register(tightFit = new Setting<>("tightFit", true));
    }

    @Override
    public List<List<Pair<String, Color>>> format(int x, int y, int z) {
      return new ArrayList<>(
          Arrays.asList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of("X", this.labelMainColor.getValue()),
                      Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of(" " + x, this.valueColor.getValue()))),
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of("Y", this.labelMainColor.getValue()),
                      Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of(" " + y, this.valueColor.getValue()))),
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of("Z", this.labelMainColor.getValue()),
                      Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of(" " + z, this.valueColor.getValue())))));
    }

    @Override
    public double getWidth(String[] fpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        return 60;
      }
      double maxWidth = 0;
      for (String string : fpsString) {
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
      }
      return maxWidth + 4;
    }

    @Override
    public double getHeight(String[] fpsString, IFontRenderer fontRenderer) {
      return fontRenderer.getFontHeight() * fpsString.length + (fpsString.length - 1) * 2 + 4;
    }

    @Override
    public String getName() {
      return "Pre Label";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new TextBox(preLabel, "Pre Label"));
      collection.add(new TextBox(postLabel, "Post Label"));
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(labelExtraColor, "Label Extra Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class CustomMode extends GPSMode {

    private final Setting<List<List<Pair<String, Color>>>> text;
    private final Setting<Boolean> tightFit;

    public CustomMode() {
      this.register(
          text =
              new Setting<>(
                  "text",
                  new ArrayList<>(
                      Arrays.asList(
                          new ArrayList<>(Collections.singletonList(Pair.of("X: $X", Color.WHITE))),
                          new ArrayList<>(Collections.singletonList(Pair.of("Y: $Y", Color.WHITE))),
                          new ArrayList<>(
                              Collections.singletonList(Pair.of("Z: $Z", Color.WHITE)))))));
      this.register(tightFit = new Setting<>("tightFit", true));
    }

    @Override
    public List<List<Pair<String, Color>>> format(int x, int y, int z) {
      List<List<Pair<String, Color>>> formattedList = new ArrayList<>();
      for (List<Pair<String, Color>> lineList : this.text.getValue()) {
        List<Pair<String, Color>> formattedLine = new ArrayList<>();
        for (Pair<String, Color> pair : lineList) {
          formattedLine.add(
              Pair.of(
                  pair.getLeft()
                      .replace("$X", String.valueOf(x))
                      .replace("$Y", String.valueOf(y))
                      .replace("$Z", String.valueOf(z)),
                  pair.getRight()));
        }
        formattedList.add(formattedLine);
      }
      return formattedList;
    }

    @Override
    public double getWidth(String[] fpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        return 60;
      }
      double maxWidth = 0;
      for (String string : fpsString) {
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
      }
      return maxWidth + 4;
    }

    @Override
    public double getHeight(String[] fpsString, IFontRenderer fontRenderer) {
      return fontRenderer.getFontHeight() * fpsString.length + (fpsString.length - 1) * 2 + 4;
    }

    @Override
    public String getName() {
      return "Custom";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new CustomTextColor(text, "Custom Text"));
    }
  }
}

package org.sorus.client.module.impl.fps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.CustomTextColor;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.Pair;

public abstract class FPSMode extends Mode {

  public abstract List<List<Pair<String, Color>>> format(int fps);

  public abstract double getWidth(String[] fpsString, IFontRenderer fontRenderer);

  public abstract double getHeight(String[] fpsString, IFontRenderer fontRenderer);

  public static class LabelPreMode extends FPSMode {

    private final Setting<String> preLabel;
    private final Setting<String> postLabel;
    private final Setting<Boolean> tightFit;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> labelExtraColor;
    private final Setting<Color> valueColor;

    public LabelPreMode() {
      this.register(preLabel = new Setting<>("preLabel", "["));
      this.register(postLabel = new Setting<>("postLabel", "]"));
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(labelExtraColor = new Setting<>("labelExtraColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(int fps) {
      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of("FPS", this.labelMainColor.getValue()),
                      Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of(" " + fps, this.valueColor.getValue())))));
    }

    @Override
    public double getWidth(String[] fpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        for (String string : fpsString) {
          maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
        }
        return maxWidth + 4;
      }
      return 60;
    }

    @Override
    public double getHeight(String[] fpsString, IFontRenderer fontRenderer) {
      return tightFit.getValue()
          ? fontRenderer.getFontHeight() * fpsString.length + (fpsString.length - 1) * 2 + 4
          : 11;
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

  public static class LabelPostMode extends FPSMode {

    private final Setting<Boolean> tightFit;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> valueColor;

    public LabelPostMode() {
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(int fps) {
      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(fps + " ", this.valueColor.getValue()),
                      Pair.of("FPS", this.labelMainColor.getValue())))));
    }

    @Override
    public double getWidth(String[] fpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        for (String string : fpsString) {
          maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
        }
        return maxWidth + 4;
      }
      return 60;
    }

    @Override
    public double getHeight(String[] fpsString, IFontRenderer fontRenderer) {
      return tightFit.getValue()
          ? fontRenderer.getFontHeight() * fpsString.length + (fpsString.length - 1) * 2 + 4
          : 11;
    }

    @Override
    public String getName() {
      return "Post Label";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class CustomMode extends FPSMode {

    private final Setting<List<List<Pair<String, Color>>>> text;

    public CustomMode() {
      this.register(
          text =
              new Setting<>(
                  "text",
                  new ArrayList<>(
                      Collections.singletonList(
                          new ArrayList<>(
                              Collections.singletonList(Pair.of("FPS: $FPS", Color.WHITE)))))));
    }

    @Override
    public List<List<Pair<String, Color>>> format(int fps) {
      List<List<Pair<String, Color>>> formattedList = new ArrayList<>();
      for (List<Pair<String, Color>> lineList : this.text.getValue()) {
        List<Pair<String, Color>> formattedLine = new ArrayList<>();
        for (Pair<String, Color> pair : lineList) {
          formattedLine.add(
              Pair.of(pair.getLeft().replace("$FPS", String.valueOf(fps)), pair.getRight()));
        }
        formattedList.add(formattedLine);
      }
      return formattedList;
    }

    @Override
    public double getWidth(String[] fpsString, IFontRenderer fontRenderer) {
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
      collection.add(new CustomTextColor(text, "Custom Text"));
    }
  }
}

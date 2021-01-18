package org.sorus.client.module.impl.cps;

import java.awt.*;
import java.util.*;
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

public abstract class CPSMode extends Mode {

  public abstract List<List<Pair<String, Color>>> format(Map<Integer, Integer> cps);

  public abstract double getWidth(String[] cpsString, IFontRenderer fontRenderer);

  public abstract double getHeight(String[] cpsString, IFontRenderer fontRenderer);

  public static class LabelPreMode extends CPSMode {

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
    public List<List<Pair<String, Color>>> format(Map<Integer, Integer> cps) {
      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of("CPS", this.labelMainColor.getValue()),
                      Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
                      Pair.of(" " + cps.get(0), this.valueColor.getValue())))));
    }

    @Override
    public double getWidth(String[] cpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        for (String string : cpsString) {
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

  public static class LabelPostMode extends CPSMode {

    private final Setting<Boolean> tightFit;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> valueColor;

    public LabelPostMode() {
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(Map<Integer, Integer> cps) {
      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(cps.get(0) + " ", this.valueColor.getValue()),
                      Pair.of("CPS", this.labelMainColor.getValue())))));
    }

    @Override
    public String getName() {
      return "Post Label";
    }

    @Override
    public double getWidth(String[] cpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        for (String string : cpsString) {
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
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class BothButtonsMode extends CPSMode {

    private final Setting<Boolean> tightFit;
    private final Setting<Color> leftValueColor;
    private final Setting<Color> dividerColor;
    private final Setting<Color> rightValueColor;

    public BothButtonsMode() {
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(leftValueColor = new Setting<>("leftValueColor", Color.WHITE));
      this.register(dividerColor = new Setting<>("dividerColor", Color.WHITE));
      this.register(rightValueColor = new Setting<>("rightValueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(Map<Integer, Integer> cps) {
      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(String.valueOf(cps.get(0)), this.leftValueColor.getValue()),
                      Pair.of(" : ", this.dividerColor.getValue()),
                      Pair.of(String.valueOf(cps.get(1)), this.rightValueColor.getValue())))));
    }

    @Override
    public String getName() {
      return "Two Buttons";
    }

    @Override
    public double getWidth(String[] cpsString, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        for (String string : cpsString) {
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
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(leftValueColor, "Left Value Color"));
      collection.add(new ColorPicker(dividerColor, "Divider Color"));
      collection.add(new ColorPicker(rightValueColor, "Right Value Color"));
    }
  }

  public static class CustomMode extends CPSMode {

    private final Setting<List<List<Pair<String, Color>>>> text;

    public CustomMode() {
      this.register(
          text =
              new Setting<>(
                  "text",
                  new ArrayList<>(
                      Collections.singletonList(
                          new ArrayList<>(
                              Collections.singletonList(Pair.of("CPS: $0", Color.WHITE)))))));
    }

    @Override
    public List<List<Pair<String, Color>>> format(Map<Integer, Integer> cps) {
      List<List<Pair<String, Color>>> formattedList = new ArrayList<>();
      for (List<Pair<String, Color>> lineList : this.text.getValue()) {
        List<Pair<String, Color>> formattedLine = new ArrayList<>();
        for (Pair<String, Color> pair : lineList) {
          String string = pair.getLeft();
          for (int integer : cps.keySet()) {
            string = string.replace("$" + integer, String.valueOf(cps.get(integer)));
          }
          formattedLine.add(Pair.of(string, pair.getRight()));
        }
        formattedList.add(formattedLine);
      }
      return formattedList;
    }

    @Override
    public double getWidth(String[] cpsString, IFontRenderer fontRenderer) {
      double maxWidth = 0;
      for (String string : cpsString) {
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
      }
      return maxWidth + 4;
    }

    @Override
    public double getHeight(String[] cpsSring, IFontRenderer fontRenderer) {
      return fontRenderer.getFontHeight() * cpsSring.length + (cpsSring.length - 1) * 2 + 4;
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

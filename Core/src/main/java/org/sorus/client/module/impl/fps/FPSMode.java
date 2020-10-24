/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.module.impl.fps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;

public abstract class FPSMode extends Mode {

  public abstract List<Pair<String, Color>> format(int fps);

  public static class LabelPreMode extends FPSMode {

    private final Setting<String> preLabel;
    private final Setting<String> postLabel;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> labelExtraColor;
    private final Setting<Color> valueColor;

    public LabelPreMode() {
      this.register(preLabel = new Setting<>("preLabel", "["));
      this.register(postLabel = new Setting<>("postLabel", "]"));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(labelExtraColor = new Setting<>("labelExtraColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<Pair<String, Color>> format(int fps) {
      return new ArrayList<>(
          Arrays.asList(
              Pair.of(this.preLabel.getValue(), this.labelExtraColor.getValue()),
              Pair.of("FPS", this.labelMainColor.getValue()),
              Pair.of(this.postLabel.getValue(), this.labelExtraColor.getValue()),
              Pair.of(" " + fps, this.valueColor.getValue())));
    }

    @Override
    public String getName() {
      return "Pre Label";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new TextBox(preLabel, "Pre Label"));
      collection.add(new TextBox(postLabel, "Post Label"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(labelExtraColor, "Label Extra Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class LabelPostMode extends FPSMode {

    private final Setting<Color> labelMainColor;
    private final Setting<Color> valueColor;

    public LabelPostMode() {
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<Pair<String, Color>> format(int fps) {
      return new ArrayList<>(
              Arrays.asList(
                      Pair.of(fps + " ", this.valueColor.getValue()),
                      Pair.of("FPS", this.labelMainColor.getValue())));
    }

    @Override
    public String getName() {
      return "Post Label";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class CustomMode extends FPSMode {

    private final Setting<List<Pair<String, Color>>> text;

    public CustomMode() {
      this.register(text = new Setting<>("text", new ArrayList<>(Collections.singletonList(Pair.of("Test", Color.WHITE)))));
    }

    @Override
    public List<Pair<String, Color>> format(int fps) {
      List<Pair<String, Color>> list = new ArrayList<>();
      for(Pair<String, Color> pair : this.text.getValue()) {
        list.add(Pair.of(pair.getLeft().replace("%FPS%", String.valueOf(fps)), pair.getRight()));
      }
      return list;
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

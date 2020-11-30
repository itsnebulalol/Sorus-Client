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

package org.sorus.client.module.impl.clock;

import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.CustomTextColor;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public abstract class ClockMode extends Mode {

  public abstract List<List<Pair<String, Color>>> format(String date);

  public abstract double getWidth(String date, IFontRenderer fontRenderer);

  public abstract double getHeight(String date, IFontRenderer fontRenderer);

  public static class Hour12 extends ClockMode {

    private final Setting<Boolean> tightFit;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> valueColor;

    public Hour12() {
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(String date) {

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date + " a");

      String formattedDate = simpleDateFormat.format(new Date());

      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(formattedDate, this.labelMainColor.getValue())))));
    }

    @Override
    public double getWidth(String date, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(date));
        return maxWidth + 4;
      }
      return 60;
    }

    @Override
    public double getHeight(String date, IFontRenderer fontRenderer) {
      return tightFit.getValue()
          ? fontRenderer.getFontHeight() : 11;
    }

    @Override
    public String getName() {
      return "Hour12";
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }

  public static class Hour24 extends ClockMode {

    private final Setting<Boolean> tightFit;
    private final Setting<Color> labelMainColor;
    private final Setting<Color> valueColor;

    public Hour24() {
      this.register(tightFit = new Setting<>("tightFit", false));
      this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
      this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    }

    @Override
    public List<List<Pair<String, Color>>> format(String date) {

      String newdate = "HH:mm";

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(newdate);

      String formattedDate = simpleDateFormat.format(new Date());

      return new ArrayList<>(
          Collections.singletonList(
              new ArrayList<>(
                  Arrays.asList(
                      Pair.of(formattedDate, this.valueColor.getValue())))));
    }

    @Override
    public String getName() {
      return "24 Hour";
    }

    @Override
    public double getWidth(String date, IFontRenderer fontRenderer) {
      if (tightFit.getValue()) {
        double maxWidth = 0;
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(date));
        return maxWidth + 4;
      }
      return 60;
    }

    @Override
    public double getHeight(String date, IFontRenderer fontRenderer) {
      return tightFit.getValue()
          ? fontRenderer.getFontHeight() : 11;
    }

    @Override
    public void addConfigComponents(Collection collection) {
      collection.add(new Toggle(tightFit, "Tight Fit"));
      collection.add(new ColorPicker(labelMainColor, "Label Color"));
      collection.add(new ColorPicker(valueColor, "Value Color"));
    }
  }
}
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

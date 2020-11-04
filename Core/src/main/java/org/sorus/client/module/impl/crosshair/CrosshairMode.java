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

package org.sorus.client.module.impl.crosshair;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.Mode;
import org.sorus.client.settings.Setting;

import java.awt.*;

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
            Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                    xCenter - thickness.getValue() / 2,
                    yCenter - lineLength.getValue() - centerGap.getValue(),
                    thickness.getValue(),
                    lineLength.getValue(),
                    color.getValue()
            );
            Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                    xCenter - thickness.getValue() / 2,
                    yCenter + centerGap.getValue(),
                    thickness.getValue(),
                    lineLength.getValue(),
                    color.getValue()
            );
            Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                    xCenter - lineLength.getValue() - centerGap.getValue(),
                    yCenter - thickness.getValue() / 2,
                    lineLength.getValue(),
                    thickness.getValue(),
                    color.getValue()
            );
            Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                    xCenter + centerGap.getValue(),
                    yCenter - thickness.getValue() / 2,
                    lineLength.getValue(),
                    thickness.getValue(),
                    color.getValue()
            );
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

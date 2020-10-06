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

package org.sorus.oneeightnine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.sorus.client.version.IScreen;

public class Screen implements IScreen {

    @Override
    public double getScaledWidth() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth_double();
    }

    @Override
    public double getScaledHeight() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight_double();
    }
    @Override
    public double getScaleFactor() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    }

    @Override
    public double getDisplayWidth() {
        return Minecraft.getMinecraft().displayWidth;
    }

    @Override
    public double getDisplayHeight() {
        return Minecraft.getMinecraft().displayHeight;
    }

}

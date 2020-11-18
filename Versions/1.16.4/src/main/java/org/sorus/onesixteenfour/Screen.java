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

package org.sorus.onesixteenfour;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.sorus.client.version.IScreen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Screen implements IScreen {

    @Override
    public double getScaledWidth() {
        return Minecraft.getInstance().getMainWindow().getScaledWidth();
    }

    @Override
    public double getScaledHeight() {
        return Minecraft.getInstance().getMainWindow().getScaledHeight();
    }
    @Override
    public double getScaleFactor() {
        return Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
    }

    @Override
    public double getDisplayWidth() {
        return Minecraft.getInstance().getMainWindow().getWidth();
    }

    @Override
    public double getDisplayHeight() {
        return Minecraft.getInstance().getMainWindow().getHeight();
    }

    @Override
    public void setIcon(InputStream x16) {
        try {
            Display.setIcon(new ByteBuffer[] {this.readImageToBuffer(x16)});
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTitle(String title) {
        Display.setTitle(title);
    }

    private ByteBuffer readImageToBuffer(InputStream var1) throws IOException {
        BufferedImage var2 = ImageIO.read(var1);
        int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), null, 0, var2.getWidth());
        ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
        for(int var8 : var3) {
            var4.putInt(var8 << 8 | var8 >> 24 & 255);
        }
        var4.flip();
        return var4;
    }

}

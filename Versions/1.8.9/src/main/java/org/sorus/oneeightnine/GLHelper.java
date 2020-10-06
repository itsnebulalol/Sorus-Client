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
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;

/**
 * Implementation of {@link IGLHelper} for 1.8.9.
 */
public class GLHelper implements IGLHelper {

    /**
     * Scales the current gl matrix.
     * @param x x to scale by
     * @param y y to scale by
     * @param z z to scale by
     */
    @Override
    public void scale(double x, double y, double z) {
        GL11.glScaled(x, y, z);
    }

    /**
     * Translates the current gl matrix.
     * @param x x to translate by
     * @param y y to translate by
     * @param z z to translze by
     */
    @Override
    public void translate(double x, double y, double z) {
        GL11.glTranslated(x, y, z);
    }

    @Override
    public void rotate(Axis axis, double value) {
        switch(axis) {
            case X:
                GL11.glRotated(value, 1, 0, 0);
                break;
            case Y:
                GL11.glRotated(value, 0, 1, 0);
                break;
            case Z:
                GL11.glRotated(value, 0, 0, 1);
                break;
        }
    }

    /**
     * Enables or disables blending.
     * @param blend to enable or disable blend
     */
    @Override
    public void blend(boolean blend) {
        if(blend) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    /**
     * Colors the current gl matrix.
     * @param red the red value (0-1)
     * @param green the green value (0-1)
     * @param blue the blue value (0-1)
     * @param alpha the alpha value (0-1)
     */
    @Override
    public void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    @Override
    public void beginScissor(double x, double y, double width, double height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        y += height;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GL11.glScissor((int) ((x * mc.displayWidth) / scaledResolution.getScaledWidth_double()),
                (int) (((scaledResolution.getScaledHeight_double() - y) * mc.displayHeight) / scaledResolution.getScaledHeight_double()),
                (int) (width * mc.displayWidth / scaledResolution.getScaledWidth_double()),
                (int) (height * mc.displayHeight / scaledResolution.getScaledHeight_double()));
    }

    @Override
    public void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    
    @Override
    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

}

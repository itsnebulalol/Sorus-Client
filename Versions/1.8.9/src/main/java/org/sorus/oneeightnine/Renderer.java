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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.version.render.IRenderer;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.render.ITTFFontRenderer;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link IRenderer} for 1.8.9.
 */
public class Renderer implements IRenderer {

    private final Map<String, FontRenderer> fontRendererCache = new HashMap<>();

    private final IVersion version;

    public Renderer(IVersion version) {
        this.version = version;
    }

    /**
     * Draws a rectangle.
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     */
    @Override
    public void drawRect(double x, double y, double width, double height, Color bottomLeftColor, Color bottomRightColor, Color topRightColor, Color topLeftColor) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4d(1, 1, 1, 1);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(x, y + height, 0).color(bottomLeftColor.getRed() / 255F, bottomLeftColor.getGreen() / 255F, bottomLeftColor.getBlue() / 255F, bottomLeftColor.getAlpha() / 255F).endVertex();
        worldRenderer.pos(x + width, y + height, 0).color(bottomRightColor.getRed() / 255F, bottomRightColor.getGreen() / 255F, bottomRightColor.getBlue() / 255F, bottomRightColor.getAlpha() / 255F).endVertex();
        worldRenderer.pos(x + width, y, 0).color(topRightColor.getRed() / 255F, topRightColor.getGreen() / 255F, topRightColor.getBlue() / 255F, topRightColor.getAlpha() / 255F).endVertex();
        worldRenderer.pos(x, y, 0).color(topLeftColor.getRed() / 255F, topLeftColor.getGreen() / 255F, topLeftColor.getBlue() / 255F, topLeftColor.getAlpha() / 255F).endVertex();
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws an arc.
     * @param x the x position
     * @param y the y position
     * @param xRadius the x radius
     * @param yRadius the y radius
     * @param startAngle the angle the arc starts at
     * @param endAngle the angle the arc ends at
     * @param color the color
     */
    @Override
    public void drawArc(double x, double y, double xRadius, double yRadius, int startAngle, int endAngle, Color color) {
        x += xRadius;
        y += yRadius;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        for(int i = startAngle; i <= endAngle; i++) {
            GL11.glVertex2d(x + Math.sin(Math.toRadians(i)) * xRadius, y + Math.cos(Math.toRadians(i)) * yRadius);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a hollow arc.
     * @param x the x position
     * @param y the y position
     * @param xRadius the x radius
     * @param yRadius the y radius
     * @param startAngle the angle the arc starts at
     * @param endAngle the angle the arc ends at
     * @param thickness the thickness of the line
     * @param color the color
     */
    @Override
    public void drawHollowArc(double x, double y, double xRadius, double yRadius, int startAngle, int endAngle, double thickness, Color color) {
        xRadius -= thickness / 2;
        yRadius -= thickness / 2;
        x += thickness / 2;
        y += thickness / 2;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth((float) (thickness * version.getScreen().getScaleFactor()));
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for(double i = startAngle; i <= endAngle; i += 1) {
            GL11.glVertex2d(x + xRadius + Math.sin(Math.toRadians(i)) * xRadius, y + yRadius + Math.cos(Math.toRadians(i)) * yRadius);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glLineWidth(1);
    }

    /**
     * Draws a string using the default minecraft font renderer.
     * @param fontLocation the location of the font
     * @param string the string to draw
     * @param x the x position
     * @param y the y position
     * @param xScale the x scale
     * @param yScale the y scale
     * @param color the color
     */
    @Override
    public void drawString(String fontLocation, String string, double x, double y, double xScale, double yScale, boolean shadow, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(xScale, yScale, 1);
        this.getFontRenderer(fontLocation).drawString(string, 0, 0, color.getRGB(), shadow);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /**
     * Gets the width of a string from the default minecraft font renderer.
     * @param fontLocation the location of the font
     * @param string the string
     * @return the width of the string
     */
    @Override
    public double getStringWidth(String fontLocation, String string) {
        return this.getFontRenderer(fontLocation).getStringWidth(string) - 1;
    }

    /**
     * Gets the character height from the default minecraft font renderer.
     * @param fontLocation the location of the font
     * @return the font height
     */
    @Override
    public double getFontHeight(String fontLocation) {
        return this.getFontRenderer(fontLocation).FONT_HEIGHT - 2;
    }

    /**
     * Gets a minecraft font renderer based on the font location.
     * @param fontLocation the location of the font
     * @return the minecraft font renderer
     */
    private FontRenderer getFontRenderer(String fontLocation) {
        FontRenderer fontRenderer = this.fontRendererCache.get(fontLocation);
        if(fontRenderer == null) {
            fontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation(fontLocation), Minecraft.getMinecraft().getTextureManager(), false);
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(fontRenderer);
            this.fontRendererCache.put(fontLocation, fontRenderer);
        }
        return fontRenderer;
    }

    @Override
    public void drawImage(String resource, double x, double y, double width, double height, double textureX, double textureY, double textureWidth, double textureHeight, Color color) {
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(width / (int) width, height / (int) height, 1);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, (int) width, (int) height, (int) width, (int) height);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void enableBlur() {
        try {
            String methodName = ObfuscationManager.getMethodName("net/minecraft/client/renderer/EntityRenderer", "loadShader", "(Lnet/minecraft/util/ResourceLocation;)V");
            Method m = EntityRenderer.class.getDeclaredMethod(methodName, ResourceLocation.class);
            m.setAccessible(true);
            m.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("blur.json"));
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableBlur() {
        Minecraft.getMinecraft().entityRenderer.loadEntityShader(null);
    }

    @Override
    public ITTFFontRenderer getFont(String location) {
        try {
            return new SlickFontRenderer(location);
        } catch(FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

}

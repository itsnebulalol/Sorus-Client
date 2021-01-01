

package org.sorus.onesixteenfour;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import org.lwjgl.opengl.GL11;
import org.sorus.client.cosmetic.TexturedQuad;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.game.IItemStack;
import org.sorus.client.version.render.IRenderer;
import org.sorus.client.version.render.ITTFFontRenderer;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class Renderer implements IRenderer {

    private final Map<String, FontRenderer> fontRendererCache = new HashMap<>();

    private final IVersion version;

    private Field theShaderGroup;
    private Field useShader;

    public Renderer(IVersion version) {
        this.version = version;

    }


    @Override
    public void drawRect(double x, double y, double width, double height, Color bottomLeftColor, Color bottomRightColor, Color topRightColor, Color topLeftColor) {

    }


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
        for(int i = startAngle; i <= endAngle; i += 1) {
            GL11.glVertex2d(x + Math.sin(Math.toRadians(i)) * xRadius, y + Math.cos(Math.toRadians(i)) * yRadius);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }


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
        GL11.glLineWidth((float) (thickness * version.getData(IScreen.class).getScaleFactor()));
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


    @Override
    public void drawString(String fontLocation, String string, double x, double y, double xScale, double yScale, boolean shadow, Color color) {

    }


    @Override
    public double getStringWidth(String fontLocation, String string) {
        return this.getFontRenderer(fontLocation).getStringWidth(string) - 1;
    }


    @Override
    public double getFontHeight(String fontLocation) {
        return this.getFontRenderer(fontLocation).FONT_HEIGHT - 2;
    }


    private FontRenderer getFontRenderer(String fontLocation) {
        FontRenderer fontRenderer = this.fontRendererCache.get(fontLocation);
        if(fontRenderer == null) {
            fontRenderer = new FontRenderer((resourceLocation) -> new Font(Minecraft.getInstance().getTextureManager(), resourceLocation));
            try {
                Field field = Minecraft.class.getDeclaredField("fontResourceMananger");
                field.setAccessible(true);
                ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(((FontResourceManager) field.get(Minecraft.getInstance())).getReloadListener());
            } catch(NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.fontRendererCache.put(fontLocation, fontRenderer);
        }
        return fontRenderer;
    }

    @Override
    public void drawPartialImage(String resource, double x, double y, double width, double height, double textureX, double textureY, double textureWidth, double textureHeight, Color color) {
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(width / textureWidth, height / textureHeight, 1);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        new AbstractGui() {}.func_238474_b_(new MatrixStack(), 0, 0, (int) textureX, (int) textureY, (int) textureWidth, (int) textureHeight);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void drawFullImage(String resource, double x, double y, double width, double height, Color color) {
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(width / (int) width, height / (int) height, 1);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        //AbstractGui.func_238464_a_(new MatrixStack(), 0, 0, 0, (int) width, (int) height, (int) width, (int) height, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void enableBlur(double amount) {

    }

    @Override
    public void disableBlur() {
        //Minecraft.getMinecraft().entityRenderer.stopUseShader();
    }

    @Override
    public ITTFFontRenderer getFont(String location) {

        return null;
    }

    @Override
    public void drawItem(IItemStack itemStack, double x, double y, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, 0);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(((ItemStackImpl) itemStack).getItemStack(), 0, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void drawQuad(TexturedQuad quad) {

    }

}

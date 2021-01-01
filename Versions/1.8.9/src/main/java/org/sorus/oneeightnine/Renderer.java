

package org.sorus.oneeightnine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.sorus.client.cosmetic.TexturedQuad;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IItemStack;
import org.sorus.client.version.render.IRenderer;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.render.ITTFFontRenderer;
import org.sorus.oneeightnine.shaders.BlurShader;

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
        try {
            String theShaderGroupName = ObfuscationManager.getFieldName("net/minecraft/client/renderer/EntityRenderer", "theShaderGroup");
            theShaderGroup = EntityRenderer.class.getDeclaredField(theShaderGroupName);
            theShaderGroup.setAccessible(true);
            String useShaderName = ObfuscationManager.getFieldName("net/minecraft/client/renderer/EntityRenderer", "useShader");
            useShader = EntityRenderer.class.getDeclaredField(useShaderName);
            useShader.setAccessible(true);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    
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
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(xScale, yScale, 1);
        this.getFontRenderer(fontLocation).drawString(string, 0, 0, color.getRGB(), shadow);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
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
            fontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation(fontLocation), Minecraft.getMinecraft().getTextureManager(), false);
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(fontRenderer);
            this.fontRendererCache.put(fontLocation, fontRenderer);
        }
        return fontRenderer;
    }

    private final Gui gui = new Gui();

    @Override
    public void drawPartialImage(String resource, double x, double y, double width, double height, double textureX, double textureY, double textureWidth, double textureHeight, Color color) {
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(width / textureWidth, height / textureHeight, 1);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        gui.drawTexturedModalRect(0, 0, (int) textureX, (int) textureY, (int) textureWidth, (int) textureHeight);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void drawFullImage(String resource, double x, double y, double width, double height, Color color) {
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(width / (int) width, height / (int) height, 1);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, (int) width, (int) height, (int) width, (int) height);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void enableBlur(double amount) {
        Minecraft mc = Minecraft.getMinecraft();
        try {
            ShaderGroup shaderGroup = new BlurShader(amount);
            theShaderGroup.set(mc.entityRenderer, shaderGroup);
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            useShader.set(mc.entityRenderer, true);
        } catch(IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableBlur() {
        Minecraft.getMinecraft().entityRenderer.stopUseShader();
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

    @Override
    public void drawItem(IItemStack itemStack, double x, double y, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(x, y, 0);
        GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
        ItemStack itemStack1 = ((ItemStackImpl) itemStack).getItemStack();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(itemStack1, 0, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void drawQuad(TexturedQuad quad) {
        WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        TexturedQuad.Vertex[] vertices = quad.getVertices();
        TexturedQuad.TexturePosition[] texturePositions = quad.getTexturePositions();
        for(int i = 0; i < 4; i++) {
            TexturedQuad.Vertex vertex = vertices[i];
            TexturedQuad.TexturePosition texturePosition = texturePositions[i];
            renderer.pos(vertex.getX(), vertex.getY(), vertex.getZ()).tex(texturePosition.getTextureX(), texturePosition.getTextureY()).normal(1, 1, 1).endVertex();

        }
        Tessellator.getInstance().draw();
    }

}

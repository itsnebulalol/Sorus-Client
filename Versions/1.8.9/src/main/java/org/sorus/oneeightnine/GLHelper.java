

package org.sorus.oneeightnine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;


public class GLHelper implements IGLHelper {


    @Override
    public void scale(double x, double y, double z) {
        GL11.glScaled(x, y, z);
    }


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


    @Override
    public void blend(boolean blend) {
        if(blend) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }


    @Override
    public void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    @Override
    public void lineWidth(double lineWidth) {
        GL11.glLineWidth((float) lineWidth);
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

    @Override
    public void bind(String resource) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(resource));
    }

    @Override
    public void push() {
        GL11.glPushMatrix();
    }

    @Override
    public void pop() {
        GL11.glPopMatrix();
    }

}



package org.sorus.onesixteenfour;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
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
        Minecraft mc = Minecraft.getInstance();
        MainWindow window = mc.getMainWindow();
        GL11.glScissor((int) ((x * window.getWidth()) / window.getScaledWidth()),
                (int) (((window.getScaledHeight() - y) * window.getHeight()) / window.getScaledHeight()),
                (int) (width * window.getWidth() / window.getScaledWidth()),
                (int) (height * window.getHeight() / window.getScaledHeight()));
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

    }

    @Override
    public void push() {

    }
    
    @Override
    public void pop() {

    }

}



package org.sorus.onesixteenfour;

import net.minecraft.client.Minecraft;
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

    }

    @Override
    public void setTitle(String title) {
        //Display.setTitle(title);
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

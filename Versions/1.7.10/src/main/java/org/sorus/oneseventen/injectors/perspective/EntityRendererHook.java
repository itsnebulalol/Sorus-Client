

package org.sorus.oneseventen.injectors.perspective;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.perspective.Perspective;
import org.sorus.client.util.MathUtil;

public class EntityRendererHook {

    public static boolean overrideMouse() {
        if(Minecraft.getMinecraft().inGameHasFocus && Display.isActive()) {
            Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
            if(!perspective.isToggled()) {
                perspective.setRotationYaw(Minecraft.getMinecraft().thePlayer.rotationYaw);
                perspective.setRotationPitch(Minecraft.getMinecraft().thePlayer.rotationPitch);
                return true;
            }
            Minecraft.getMinecraft().mouseHelper.mouseXYChange();
            float f1 = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f2 = (f1 * f1 * f1 * 8.0f);
            float f3 = (float) Minecraft.getMinecraft().mouseHelper.deltaX * f2;
            float f4 = (float) Minecraft.getMinecraft().mouseHelper.deltaY * f2;
            if(perspective.invertYaw()) {
                f3 = -f3;
            }
            if(perspective.invertPitch()) {
                f4 = -f4;
            }
            perspective.setRotationYaw(perspective.getRotationYaw() + f3 * 0.15f);
            perspective.setRotationPitch(perspective.getRotationPitch() + f4 * 0.15f);
            perspective.setRotationPitch((float) MathUtil.clamp(perspective.getRotationPitch(), -90, 90));
        }
        return false;
    }

    public static float getRotationPitch() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationPitch() : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public static float getRotationYaw() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationYaw() : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

}

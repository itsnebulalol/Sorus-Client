

package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.perspective.Perspective;
import org.sorus.client.util.MathUtil;

public class EntityRendererHook {

    public static float getRotationPitch() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() || Minecraft.getMinecraft().thePlayer == null ? perspective.getRotationPitch() : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public static float getRotationYaw() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() || Minecraft.getMinecraft().thePlayer == null ? perspective.getRotationYaw() : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

    public static void setAngles(float yaw, float pitch) {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        if(!perspective.isToggled()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(player != null) {
                player.setAngles(yaw, pitch);
                perspective.setRotationYaw(player.rotationYaw);
                perspective.setRotationPitch(player.rotationPitch);
            }
        } else {
            perspective.setRotationYaw(perspective.getRotationYaw() + (yaw * 0.15F * (perspective.invertYaw() ? -1 : 1)));
            perspective.setRotationPitch(perspective.getRotationPitch() + (pitch * 0.15F * (perspective.invertYaw() ? -1 : 1)));
        }
    }

}

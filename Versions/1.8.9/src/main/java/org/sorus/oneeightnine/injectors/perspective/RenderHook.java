package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.Minecraft;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.perspective.Perspective;

public class RenderHook {

    public static float getRotationPitch() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationPitch() : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public static float getRotationYaw() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationYaw() : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

}

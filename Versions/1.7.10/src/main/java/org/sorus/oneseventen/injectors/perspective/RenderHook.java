package org.sorus.oneseventen.injectors.perspective;

import net.minecraft.client.Minecraft;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.perspective.Perspective;

public class RenderHook {

    public static float getRotationPitch() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        float renderPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            renderPitch = -renderPitch;
        }
        return perspective.isToggled() ? perspective.getRotationPitch() : renderPitch;
    }

    public static float getRotationYaw() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        float renderYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            if(renderYaw > 180) {
                renderYaw -= 180;
            } else {
                renderYaw += 180;
            }
        }
        return perspective.isToggled() ? perspective.getRotationYaw() : renderYaw;
    }

}

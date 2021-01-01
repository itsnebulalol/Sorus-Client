

package org.sorus.oneeightnine.injectors.timechanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.timechanger.TimeChanger;

public class WorldHooks {

    public static long getAlteredTime() {
        TimeChanger timeChanger = Sorus.getSorus().getModuleManager().getModule(TimeChanger.class);
        String className = new Exception().getStackTrace()[3].getClassName();
        if(timeChanger.isEnabled() && (className.equals(EntityRenderer.class.getName()) || className.equals(RenderGlobal.class.getName()))) {
            return timeChanger.getTime();
        }
        if(Minecraft.getMinecraft().theWorld == null) {
            return 0;
        }
        return Minecraft.getMinecraft().theWorld.getWorldTime();
    }

}

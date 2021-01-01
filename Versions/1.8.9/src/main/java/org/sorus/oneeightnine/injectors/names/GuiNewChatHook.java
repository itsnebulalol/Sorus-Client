

package org.sorus.oneeightnine.injectors.names;

import net.minecraft.client.Minecraft;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.names.Names;

public class GuiNewChatHook {

    public static String formatChatMessage(String message) {
        Names names = Sorus.getSorus().getModuleManager().getModule(Names.class);
        if(names.customName()) {
            return message.replace(Minecraft.getMinecraft().thePlayer.getName(), names.getCustomName().replace("&", "§"));
        }
        return message;
    }

}

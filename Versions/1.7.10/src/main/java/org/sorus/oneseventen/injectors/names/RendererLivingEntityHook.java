

package org.sorus.oneseventen.injectors.names;

import net.minecraft.client.entity.EntityPlayerSP;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.names.Names;

public class RendererLivingEntityHook {

    public static String getName(EntityPlayerSP player) {
        Names names = Sorus.getSorus().getModuleManager().getModule(Names.class);
        if(names.customName()) {
            String customName = names.getCustomName().replace("&", "§");
            if(names.overrideNameTag()) {
                return customName;
            } else {
                return player.getFormattedCommandSenderName().getFormattedText().replace(player.getGameProfile().getName(), customName);
            }
        }
        return player.getFormattedCommandSenderName().getFormattedText();
    }

    public static boolean renderSelfName() {
        return Sorus.getSorus().getModuleManager().getModule(Names.class).renderSelfName();
    }

}

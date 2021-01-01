

package org.sorus.oneeightnine.injectors.names;

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
                return player.getDisplayName().getFormattedText().replace(player.getName(), customName);
            }
        }
        return player.getDisplayName().getFormattedText();
    }

    public static boolean renderSelfName() {
        return Sorus.getSorus().getModuleManager().getModule(Names.class).renderSelfName();
    }

}

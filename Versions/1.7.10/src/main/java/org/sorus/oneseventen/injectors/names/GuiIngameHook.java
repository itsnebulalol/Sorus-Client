

package org.sorus.oneseventen.injectors.names;

import net.minecraft.client.Minecraft;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.names.Names;

public class GuiIngameHook {

    public static String getName(String name) {
        Names names = Sorus.getSorus().getModuleManager().getModule(Names.class);
        if(name.equals(Minecraft.getMinecraft().getSession().getUsername()) && names.customName()) {
            return names.getCustomName();
        }
        return name;
    }

}

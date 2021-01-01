

package org.sorus.oneeightnine.injectors.names;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.names.Names;
import org.sorus.client.obfuscation.ObfuscationManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuiPlayerTabOverlayHook {

    private static Field locationSkin;
    private static Field name;

    static {
        try {
            locationSkin = NetworkPlayerInfo.class.getDeclaredField(ObfuscationManager.getFieldName("net/minecraft/client/network/NetworkPlayerInfo", "locationSkin"));
            locationSkin.setAccessible(true);
            name = GameProfile.class.getDeclaredField("name");
            name.setAccessible(true);
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Collection<NetworkPlayerInfo> transformInfo(Collection<NetworkPlayerInfo> infos) {
        Names names = Sorus.getSorus().getModuleManager().getModule(Names.class);
        if(!names.customName()) {
            return infos;
        }
        List<NetworkPlayerInfo> infosResult = new ArrayList<>();
        String customName = names.getCustomName().replace("&", "§");
        for(NetworkPlayerInfo info : infos) {
            if(info.getGameProfile().getName().equals(Minecraft.getMinecraft().getSession().getUsername())) {
                info = new FakeNetworkPlayerInfo(info, customName, names.overrideNameTag());
            }
            infosResult.add(info);
        }
        return infosResult;
    }

}

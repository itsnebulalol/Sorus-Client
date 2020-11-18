/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

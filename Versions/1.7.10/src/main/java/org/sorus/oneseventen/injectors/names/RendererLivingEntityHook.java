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

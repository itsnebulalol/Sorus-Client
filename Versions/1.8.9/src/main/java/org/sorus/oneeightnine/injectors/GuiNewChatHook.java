

package org.sorus.oneeightnine.injectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.chat.ChatEvent;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.util.Reflection;

import java.util.List;

public class GuiNewChatHook {

    public static void onSetChatLine() {
        List<ChatLine> chatLines = Reflection.getField(Minecraft.getMinecraft().ingameGUI.getChatGUI(), ObfuscationManager.getFieldName("net/minecraft/client/gui/GuiNewChat", "chatLines"));
        Sorus.getSorus().getEventManager().post(new ChatEvent(chatLines.get(0).getChatComponent().getFormattedText()));
    }

}

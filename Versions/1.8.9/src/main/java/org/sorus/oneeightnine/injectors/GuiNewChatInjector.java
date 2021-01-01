

package org.sorus.oneeightnine.injectors;

import net.minecraft.client.gui.GuiNewChat;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

@Hook("net/minecraft/client/gui/GuiNewChat")
public class GuiNewChatInjector extends Injector<GuiNewChat> {

    public GuiNewChatInjector(GuiNewChat that) {
        super(that);
    }

    @Inject(name = "setChatLine", desc = "(Lnet/minecraft/util/IChatComponent;IIZ)V", at = @At("RETURN"))
    public void setChatLine() {
        GuiNewChatHook.onSetChatLine();
    }

}

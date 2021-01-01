

package org.sorus.oneseventen.injectors;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;
import org.sorus.oneseventen.util.input.InputMap;
import org.sorus.oneseventen.util.input.MouseHandler;


@Hook("net/minecraft/client/gui/GuiScreen")
public class GuiScreenInjector extends Injector<GuiScreen> {

    public GuiScreenInjector(GuiScreen that) {
        super(that);
    }

    
    @Inject(name = "handleMouseInput", at = @At("HEAD"))
    public void handleMouseInput() {
        MouseHandler.handleMouse(InputMap.getButton(Mouse.getEventButton()), Mouse.getEventButtonState());
    }

}

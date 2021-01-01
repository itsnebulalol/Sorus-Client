

package org.sorus.oneseventen.injectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.KeyReleaseEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;
import org.sorus.client.version.game.GUIType;
import org.sorus.oneseventen.GuiBlank;
import org.sorus.oneseventen.util.input.InputMap;
import org.sorus.oneseventen.util.input.MouseHandler;


@Hook("net/minecraft/client/Minecraft")
public class MinecraftInjector extends Injector<Minecraft> {

    public MinecraftInjector(Minecraft that) {
        super(that);
    }

    
    @Inject(name = "startGame", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/OpenGlHelper;initializeTextures()V", shift = At.Shift.AFTER))
    public void startGame() {
        Sorus.getSorus().getEventManager().post(new StartEvent());
    }

    
    @Inject(name = "runTick", at = @At("RETURN"))
    public void runTick() {
        Sorus.getSorus().getEventManager().post(new TickEvent());
    }

    
    @Inject(name = "runTick", at = @At(value = "INVOKE", target = "org/lwjgl/input/Mouse;getEventButton()I", shift = At.Shift.BEFORE))
    public void runTickMouse() {
        MouseHandler.handleMouse(InputMap.getButton(Mouse.getEventButton()), Mouse.getEventButtonState());
    }

    
    @Inject(name = "dispatchKeypresses", at = @At(value = "INVOKE", target = "org/lwjgl/input/Keyboard;getEventKey()I", shift = At.Shift.BEFORE, ordinal = 0))
    public void dispatchKeypresses() {
        int eventKey = Keyboard.getEventKey();
        if(Keyboard.getEventKeyState()) {
            Sorus.getSorus().getEventManager().post(new KeyPressEvent(InputMap.getKey(eventKey), Keyboard.getEventCharacter(), Keyboard.isRepeatEvent()));
        } else {
            Sorus.getSorus().getEventManager().post(new KeyReleaseEvent(InputMap.getKey(eventKey)));
        }
    }

    @Inject(name = "displayGuiScreen", desc = "(Lnet/minecraft/client/gui/GuiScreen;)V", at = @At(value = "RETURN"))
    public void displayGuiScreen() {
        GUIType type = GUIType.UNDEFINED;
        GuiScreen guiScreen = that.currentScreen;
        if(guiScreen == null) {
            type = GUIType.NULL;
        } else if(guiScreen.getClass().equals(GuiMainMenu.class)) {
            type = GUIType.MAIN_MENU;
        } else if(guiScreen.getClass().equals(GuiInventory.class)) {
            type = GUIType.INVENTORY;
        } else if(guiScreen.getClass().equals(GuiContainerCreative.class)) {
            type = GUIType.INVENTORY_CREATIVE;
        } else if(guiScreen.getClass().equals(GuiCrafting.class)) {
            type = GUIType.CRAFTING;
        } else if(guiScreen.getClass().equals(GuiBlank.class)) {
            type = GUIType.BLANK;
        }
        Sorus.getSorus().getEventManager().post(new GuiSwitchEvent(type));
    }

}

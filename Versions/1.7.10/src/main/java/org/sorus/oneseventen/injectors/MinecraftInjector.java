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
import org.sorus.oneseventen.GuiBlank;
import org.sorus.oneseventen.util.input.InputMap;
import org.sorus.oneseventen.util.input.MouseHandler;

/**
 * Injector for the Minecraft class (mcp mappings).
 */
@Hook("net/minecraft/client/Minecraft")
public class MinecraftInjector extends Injector<Minecraft> {

    public MinecraftInjector(Minecraft that) {
        super(that);
    }

    /**
     * Calls the {@link StartEvent} at the end of the method.
     */
    @Inject(name = "startGame", at = @At("RETURN"))
    public void startGame() {
        Sorus.getSorus().getEventManager().post(new StartEvent());
    }

    /**
     * Calls the {@link TickEvent} at the end of the method.
     */
    @Inject(name = "runTick", at = @At("RETURN"))
    public void runTick() {
        Sorus.getSorus().getEventManager().post(new TickEvent());
    }

    /**
     * Handles mouse input while a GuiScreen is not currently open (ingame).
     */
    @Inject(name = "runTick", at = @At(value = "INVOKE", target = "org/lwjgl/input/Mouse;getEventButton()I", shift = At.Shift.BEFORE))
    public void runTickMouse() {
        MouseHandler.handleMouse(InputMap.getButton(Mouse.getEventButton()), Mouse.getEventButtonState());
    }

    /**
     * Handles keypresses and calls the correct event based on the situation at the start of the method.
     */
    @Inject(name = "dispatchKeypresses", at = @At(value = "INVOKE", target = "org/lwjgl/input/Keyboard;getEventKey()I", shift = At.Shift.BEFORE, ordinal = 0))
    public void dispatchKeypresses() {
        int eventKey = Keyboard.getEventKey();
        if(Keyboard.getEventKeyState()) {
            Sorus.getSorus().getEventManager().post(new KeyPressEvent(InputMap.getKey(eventKey), Keyboard.getEventCharacter(), Keyboard.isRepeatEvent()));
        } else {
            Sorus.getSorus().getEventManager().post(new KeyReleaseEvent(InputMap.getKey(eventKey), Keyboard.isRepeatEvent()));
        }
    }

    @Inject(name = "displayGuiScreen", desc = "(Lnet/minecraft/client/gui/GuiScreen;)V", at = @At(value = "RETURN"))
    public void displayGuiScreen() {
        GuiSwitchEvent.Type type = GuiSwitchEvent.Type.UNDEFINED;
        GuiScreen guiScreen = that.currentScreen;
        if(guiScreen == null) {
            type = GuiSwitchEvent.Type.NULL;
        } else if(guiScreen.getClass().equals(GuiMainMenu.class)) {
            type = GuiSwitchEvent.Type.MAIN_MENU;
        } else if(guiScreen.getClass().equals(GuiInventory.class)) {
            type = GuiSwitchEvent.Type.INVENTORY;
        } else if(guiScreen.getClass().equals(GuiContainerCreative.class)) {
            type = GuiSwitchEvent.Type.INVENTORY_CREATIVE;
        } else if(guiScreen.getClass().equals(GuiCrafting.class)) {
            type = GuiSwitchEvent.Type.CRAFTING;
        } else if(guiScreen.getClass().equals(GuiBlank.class)) {
            type = GuiSwitchEvent.Type.BLANK;
        }
        Sorus.getSorus().getEventManager().post(new GuiSwitchEvent(type));
    }

}

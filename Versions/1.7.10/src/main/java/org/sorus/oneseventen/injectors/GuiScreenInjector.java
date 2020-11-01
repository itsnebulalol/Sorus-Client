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

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;
import org.sorus.oneseventen.util.input.InputMap;
import org.sorus.oneseventen.util.input.MouseHandler;

/**
 * Injector for the GuiScreen class.
 */
@Hook("net/minecraft/client/gui/GuiScreen")
public class GuiScreenInjector extends Injector<GuiScreen> {

    public GuiScreenInjector(GuiScreen that) {
        super(that);
    }

    /**
     * Handles mouse input while a GuiScreen is currently open.
     */
    @Inject(name = "handleMouseInput", at = @At("HEAD"))
    public void handleMouseInput() {
        MouseHandler.handleMouse(InputMap.getButton(Mouse.getEventButton()), Mouse.getEventButtonState());
    }

}

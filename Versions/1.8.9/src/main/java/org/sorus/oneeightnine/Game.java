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

package org.sorus.oneeightnine;import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.sorus.client.version.game.IEntity;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.game.PerspectiveMode;

public class Game implements IGame {

    @Override
    public boolean isIngame() {
        return Minecraft.getMinecraft().currentScreen == null;
    }

    @Override
    public boolean shouldRenderHUDS() {
        return this.isIngame() || Minecraft.getMinecraft().currentScreen instanceof GuiChat || Minecraft.getMinecraft().currentScreen instanceof GuiBlank;
    }

    @Override
    public void displayBlankGUI() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBlank());
    }

    @Override
    public void removeBlankGUI() {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiBlank) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public int getFPS() {
        return Minecraft.getDebugFPS();
    }

    @Override
    public IEntity getPlayer() {
        return new EntityImpl(Minecraft.getMinecraft().thePlayer);
    }

    @Override
    public PerspectiveMode getPerspective() {
        switch(Minecraft.getMinecraft().gameSettings.thirdPersonView) {
            case 0:
                return PerspectiveMode.FIRST_PERSON;
            case 1:
                return PerspectiveMode.THIRD_PERSON_BACK;
            case 2:
                return PerspectiveMode.THIRD_PERSON_FRONT;
            default:
                return null;
        }
    }

    @Override
    public void setPerspective(PerspectiveMode perspective) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        switch(perspective) {
            case FIRST_PERSON:
                gameSettings.thirdPersonView = 0;
                break;
            case THIRD_PERSON_BACK:
                gameSettings.thirdPersonView = 1;
                break;
            case THIRD_PERSON_FRONT:
                gameSettings.thirdPersonView = 2;
                break;
        }
    }

    @Override
    public void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
    }

    @Override
    public int getPing() {
        return Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID()).getResponseTime();
    }
}

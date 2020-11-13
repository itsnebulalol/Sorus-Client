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

package org.sorus.client.module.impl.customscreens;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.module.Module;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.module.impl.customscreens.mainmenu.MainMenu;
import org.sorus.client.version.game.GUIType;

public class SorusMainMenuVisibler extends Module {

    public SorusMainMenuVisibler() {
        super("Custom Main Menu");
    }

    @Override
    public void onLoad() {
        Sorus.getSorus().getEventManager().register(this);
        super.onLoad();
    }

    @Override
    public VersionDecision getVersions() {
        return new VersionDecision.Allow();
    }

    @EventInvoked
    public void override(GuiSwitchEvent e) {
        if(e.getType().equals(GUIType.MAIN_MENU)) {
            Sorus.getSorus().getVersion().getGame().display(GUIType.BLANK);
            Sorus.getSorus().getGUIManager().open(new MainMenu());
        } else {
            if(Sorus.getSorus().getGUIManager().isScreenOpen(MainMenu.class)) {
                Sorus.getSorus().getGUIManager().close(Sorus.getSorus().getGUIManager().getCurrentScreen(MainMenu.class));
            }
        }
    }

}

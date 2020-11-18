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

package org.sorus.client.module.impl.customscreens.mainmenu;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.input.Key;

import java.awt.*;

public class MainMenu extends Screen {

    private final Panel main;
    private final Component image;

    public MainMenu() {
        main = new Panel();
        main.add(image = new Image().resource("sorus/modules/custommenus/background.png").size(1940, 1100));
        main.add(new Rectangle().size(175, 175).smooth(87.5).color(DefaultTheme.getMedbackgroundLayerColor()).position(872.5, 302.5));
        main.add(new Image().resource("sorus/modules/custommenus/logo.png").size(150, 150).color(DefaultTheme.getForegroundLayerColor()).position(885, 315));
        main.add(new MainMenuMainElement.Singleplayer().position(735, 500));
        main.add(new MainMenuMainElement.Multiplayer().position(735, 600));
        Collection bottomBar = new Collection();
        bottomBar.add(new Rectangle().size(300, 100).smooth(50).color(DefaultTheme.getMedbackgroundLayerColor()));
        bottomBar.position(810, 965);
        bottomBar.add(new MainMenuBottomElement.LanguagesButton().position(12.5, 12.5));
        bottomBar.add(new MainMenuBottomElement.SettingsButton().position(112.5, 12.5));
        bottomBar.add(new MainMenuBottomElement.ExitButton().position(212.5, 12.5));
        main.add(bottomBar);
    }

    @Override
    public void onRender() {
        double xPercent = Sorus.getSorus().getVersion().getInput().getMouseX() / Sorus.getSorus().getVersion().getScreen().getScaledWidth();
        double yPercent = Sorus.getSorus().getVersion().getInput().getMouseY() / Sorus.getSorus().getVersion().getScreen().getScaledHeight();
        image.position(-5 + (xPercent - 0.5) * 10, -5 + (yPercent - 0.5) * 10);
        this.main.scale(
                Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
                Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
        this.main.onRender(this);
    }

    @Override
    public void keyTyped(Key key, boolean repeat) {
        if(key == Key.F5) {
            Sorus.getSorus().getGUIManager().close(this);
            Sorus.getSorus().getGUIManager().open(new MainMenu());
        }
    }

    @Override
    public void onExit() {
        super.onExit();
        this.main.onRemove();
    }
}

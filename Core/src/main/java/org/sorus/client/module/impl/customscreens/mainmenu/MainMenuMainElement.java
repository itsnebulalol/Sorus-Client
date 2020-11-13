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
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.game.GUIType;

public abstract class MainMenuMainElement extends Collection {

    private final Collection collection = new Collection();
    private long prevRenderTime;
    private double expandedPercent;

    public MainMenuMainElement(String name) {
        collection.add(new Rectangle().size(700, 90).color(DefaultTheme.getMedbackgroundLayerColor()));
        Text text;
        collection.add(text = new Text().text(name).fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer()).scale(4, 4).color(DefaultTheme.getForegroundLayerColor()));
        text.position(350 - text.width() * 4 / 2, 45 - text.height() * 4 / 2);
        this.add(collection);
        Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
        long renderTime = System.currentTimeMillis();
        long deltaTime = renderTime - prevRenderTime;
        boolean hovered = this.isHovered(Sorus.getSorus().getVersion().getInput().getMouseX(), Sorus.getSorus().getVersion().getInput().getMouseY());
        expandedPercent = MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.015, 0, 1);
        collection.position(-expandedPercent * 4, -expandedPercent * 0.5).scale(1 + 0.01 * expandedPercent, 1 + 0.01 * expandedPercent);
        prevRenderTime = renderTime;
        super.onRender();
    }

    @Override
    public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
        super.onRemove();
    }

    private boolean isHovered(double x, double y) {
        return x > this.absoluteX() && x < this.absoluteX() + 700 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 90 * this.absoluteYScale();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
        if(this.isHovered(e.getX(), e.getY())) {
            this.onClick();
        }
    }

    public abstract void onClick();

    public static class Singleplayer extends MainMenuMainElement {

        public Singleplayer() {
            super("Singleplayer");
        }

        @Override
        public void onClick() {
            Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
            Sorus.getSorus().getVersion().getGame().display(GUIType.VIEW_WORLDS);
        }

    }

    public static class Multiplayer extends MainMenuMainElement {

        public Multiplayer() {
            super("Multiplayer");
        }

        @Override
        public void onClick() {
            Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
            Sorus.getSorus().getVersion().getGame().display(GUIType.VIEW_SERVERS);
        }

    }

}

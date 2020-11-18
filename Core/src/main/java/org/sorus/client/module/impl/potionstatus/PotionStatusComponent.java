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

package org.sorus.client.module.impl.potionstatus;

import org.sorus.client.Sorus;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IPotionEffect;

import java.awt.*;
import java.util.List;

public class PotionStatusComponent extends Component {

    private final Setting<Color> backgroundColor;
    private final Setting<Color> nameAmplifierColor;
    private final Setting<Color> durationColor;

    private List<IPotionEffect> potionEffects;

    private double width, height;

    public PotionStatusComponent() {
        super("POTION STATUS");
        this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
        this.register(nameAmplifierColor = new Setting<>("nameAmplifierColor", Color.WHITE));
        this.register(durationColor = new Setting<>("durationColor", new Color(175, 175, 175)));
    }

    @Override
    public void update(boolean dummy) {
        this.potionEffects = Sorus.getSorus().getVersion().getGame().getPlayer().getActivePotionEffects();
    }

    @Override
    public void render(double x, double y, boolean dummy) {
        Sorus.getSorus().getGUIManager().getRenderer().drawRect(x, y, width, height, backgroundColor.getValue());
        int i = 0;
        for(IPotionEffect potionEffect : potionEffects) {
            potionEffect.drawIcon(x + 2, y + i * 19 + 2, 20, 20);
            Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer().drawString(potionEffect.getName() + " " + potionEffect.getAmplifier(), x + 25, y + i * 19 + 4, 1, 1, false, this.nameAmplifierColor.getValue());
            Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer().drawString(potionEffect.getDuration(), x + 25, y + i * 19 + 13, 1, 1, false, this.durationColor.getValue());
            i++;
        }
    }

    @Override
    public double getWidth() {
        double width = 0;
        for(IPotionEffect potionEffect : potionEffects) {
            width = Math.max(width, Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer().getStringWidth(potionEffect.getName() + " " + potionEffect.getAmplifier()) + 27);
        }
        return this.width = width;
    }

    @Override
    public double getHeight() {
        return this.height = potionEffects.size() * 19 + 4;
    }

}

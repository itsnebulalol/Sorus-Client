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

package org.sorus.oneeightnine;

import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.sorus.client.Sorus;
import org.sorus.client.version.game.IPotionEffect;

import java.awt.*;
import java.util.Arrays;

public class PotionEffectImpl implements IPotionEffect {

    private final PotionEffect potionEffect;

    public PotionEffectImpl(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
    }

    @Override
    public String getName() {
        return I18n.format(this.potionEffect.getEffectName());
    }

    @Override
    public String getAmplifier() {
        switch(this.potionEffect.getAmplifier()) {
            case 0:
                return "I";
            case 1:
                return "II";
            case 2:
                return "III";
            case 3:
                return "IV";
            case 4:
                return "V";
            case 5:
                return "VI";
            case 6:
                return "VII";
            case 7:
                return "VIII";
            case 8:
                return "IX";
            case 9:
                return "X";
            default:
                return "If you see this please report this bug.";
        }
    }

    @Override
    public String getDuration() {
        return Potion.getDurationString(this.potionEffect);
    }

    @Override
    public void drawIcon(double x, double y, double width, double height) {
        int index = Potion.potionTypes[potionEffect.getPotionID()].getStatusIconIndex();
        Sorus.getSorus().getGUIManager().getRenderer().drawPartialImage("textures/gui/container/inventory.png", x, y, width, height, index % 8 * 18, 198 + index / 8 * 18, 18, 18, Color.WHITE);
    }

}

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

package org.sorus.onesixteenfour;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import org.lwjgl.opengl.GL11;
import org.sorus.client.version.game.IPotionEffect;

public class PotionEffectImpl implements IPotionEffect {

    private final EffectInstance effect;

    public PotionEffectImpl(EffectInstance potionEffect) {
        this.effect = potionEffect;
    }

    @Override
    public String getName() {
        return I18n.format(this.effect.getEffectName());
    }

    @Override
    public String getAmplifier() {
        switch(this.effect.getAmplifier()) {
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
        return EffectUtils.getPotionDurationString(this.effect, 1);
    }

    @Override
    public void drawIcon(double x, double y, double width, double height) {
        Effect effect = this.effect.getPotion();
        TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getPotionSpriteUploader().getSprite(effect);
        Minecraft.getInstance().getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        AbstractGui.func_238470_a_(new MatrixStack(), 0, 0, 0, 18, 18, textureatlassprite);
        GL11.glPopMatrix();
    }

}

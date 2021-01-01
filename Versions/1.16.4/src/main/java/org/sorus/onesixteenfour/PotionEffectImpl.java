

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
                return "";
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

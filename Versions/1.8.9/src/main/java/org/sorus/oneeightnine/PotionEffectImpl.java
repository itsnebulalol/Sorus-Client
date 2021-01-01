

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
                return "";
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

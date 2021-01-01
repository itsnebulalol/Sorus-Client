

package org.sorus.oneeightnine;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Timer;
import org.sorus.client.util.Reflection;
import org.sorus.client.version.game.ILivingEntity;
import org.sorus.client.version.game.IPotionEffect;

import java.util.ArrayList;
import java.util.List;

public class EntityLivingBaseImpl extends EntityImpl implements ILivingEntity {

    public EntityLivingBaseImpl(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public List<IPotionEffect> getActivePotionEffects() {
        List<IPotionEffect> potionEffects = new ArrayList<>();
        for(PotionEffect potionEffect : ((EntityLivingBase) this.entity).getActivePotionEffects()) {
            potionEffects.add(new PotionEffectImpl(potionEffect));
        }
        return potionEffects;
    }

    @Override
    public double getBodyRotation() {
        return ((EntityLivingBase) entity).prevRenderYawOffset + (((EntityLivingBase) entity).renderYawOffset - ((EntityLivingBase) entity).prevRenderYawOffset) * ((Timer) Reflection.getField(Minecraft.getMinecraft(), "timer")).renderPartialTicks;
    }

}

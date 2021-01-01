

package org.sorus.onesixteenfour;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import org.sorus.client.version.game.ILivingEntity;
import org.sorus.client.version.game.IPotionEffect;

import java.util.ArrayList;
import java.util.List;

public class LivingEntityImpl extends EntityImpl implements ILivingEntity {

    public LivingEntityImpl(LivingEntity entity) {
        super(entity);
    }

    @Override
    public List<IPotionEffect> getActivePotionEffects() {
        List<IPotionEffect> potionEffects = new ArrayList<>();
        for(EffectInstance effect : ((LivingEntity) this.entity).getActivePotionEffects()) {
            potionEffects.add(new PotionEffectImpl(effect));
        }
        return potionEffects;
    }

    @Override
    public double getBodyRotation() {
        return 0;
    }

}

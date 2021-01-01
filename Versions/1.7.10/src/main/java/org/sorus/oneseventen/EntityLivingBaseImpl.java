

package org.sorus.oneseventen;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.sorus.client.version.game.ILivingEntity;
import org.sorus.client.version.game.IPotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityLivingBaseImpl extends EntityImpl implements ILivingEntity {

    public EntityLivingBaseImpl(Entity entity) {
        super(entity);
    }

    @Override
    public List<IPotionEffect> getActivePotionEffects() {
        List<IPotionEffect> potionEffects = new ArrayList<>();
        for(PotionEffect potionEffect : (Collection<PotionEffect>) ((EntityLivingBase) this.entity).getActivePotionEffects()) {
            potionEffects.add(new PotionEffectImpl(potionEffect));
        }
        return potionEffects;
    }

    @Override
    public double getBodyRotation() {
        return 0;
    }

}

package org.sorus.oneeightnine.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.sorus.oneeightnine.EntityImpl;
import org.sorus.oneeightnine.EntityLivingBaseImpl;
import org.sorus.oneeightnine.PlayerImpl;

public class Util {

    public static EntityImpl getEntity(Entity entity) {
        if(entity instanceof EntityLivingBase) {
            if(entity instanceof EntityPlayer) {
                return new PlayerImpl((EntityPlayer) entity);
            }
            return new EntityLivingBaseImpl((EntityLivingBase) entity);
        }
        return new EntityImpl(entity);
    }

}

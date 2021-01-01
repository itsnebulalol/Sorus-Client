

package org.sorus.onesixteenfour;

import net.minecraft.entity.Entity;
import org.sorus.client.version.game.EntityType;
import org.sorus.client.version.game.IEntity;

public class EntityImpl implements IEntity {

    protected final Entity entity;

    public EntityImpl(Entity entity) {
        this.entity = entity;
    }

    @Override
    public double getX() {
        return entity.getPosX();
    }

    @Override
    public double getY() {
        return entity.getPosY();
    }

    @Override
    public double getZ() {
        return entity.getPosZ();
    }

    @Override
    public boolean exists() {
        return entity != null;
    }

    @Override
    public EntityType getType() {
        return null;
    }
}

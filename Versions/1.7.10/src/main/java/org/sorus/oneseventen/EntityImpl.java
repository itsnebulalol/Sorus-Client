

package org.sorus.oneseventen;

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
        return entity.posX;
    }

    @Override
    public double getY() {
        return entity.posY;
    }

    @Override
    public double getZ() {
        return entity.posZ;
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

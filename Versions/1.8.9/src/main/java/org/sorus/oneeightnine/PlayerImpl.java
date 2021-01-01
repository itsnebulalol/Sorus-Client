

package org.sorus.oneeightnine;

import net.minecraft.entity.player.EntityPlayer;
import org.sorus.client.version.game.EntityType;
import org.sorus.client.version.game.IInventoryPlayer;
import org.sorus.client.version.game.IPlayer;

public class PlayerImpl extends EntityLivingBaseImpl implements IPlayer {

    public PlayerImpl(EntityPlayer player) {
        super(player);
    }

    @Override
    public IInventoryPlayer getInventory() {
        return new InventoryPlayerImpl(((EntityPlayer) this.entity).inventory);
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }
}

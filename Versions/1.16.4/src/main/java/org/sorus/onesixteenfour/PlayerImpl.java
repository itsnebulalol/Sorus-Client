

package org.sorus.onesixteenfour;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.sorus.client.version.game.IInventoryPlayer;
import org.sorus.client.version.game.IPlayer;

public class PlayerImpl extends LivingEntityImpl implements IPlayer {

    public PlayerImpl(ClientPlayerEntity player) {
        super(player);
    }

    @Override
    public IInventoryPlayer getInventory() {
        return new InventoryPlayerImpl(((PlayerEntity) this.entity).inventory);
    }

}

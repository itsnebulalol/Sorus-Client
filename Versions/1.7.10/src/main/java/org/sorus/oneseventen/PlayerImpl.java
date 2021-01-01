

package org.sorus.oneseventen;

import net.minecraft.entity.player.EntityPlayer;
import org.sorus.client.version.game.IInventoryPlayer;
import org.sorus.client.version.game.IPlayer;
import org.sorus.client.version.game.IPotionEffect;

import java.util.List;

public class PlayerImpl extends EntityLivingBaseImpl implements IPlayer {

    public PlayerImpl(EntityPlayer player) {
        super(player);
    }

    @Override
    public IInventoryPlayer getInventory() {
        return new InventoryPlayerImpl(((EntityPlayer) this.entity).inventory);
    }

}

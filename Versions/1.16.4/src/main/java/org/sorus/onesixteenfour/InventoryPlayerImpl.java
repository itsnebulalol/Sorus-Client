

package org.sorus.onesixteenfour;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.sorus.client.version.game.IInventoryPlayer;
import org.sorus.client.version.game.IItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryPlayerImpl implements IInventoryPlayer {

    private final PlayerInventory inventory;

    public InventoryPlayerImpl(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<IItemStack> getArmor() {
        List<ItemStack> itemStacks = this.inventory.armorInventory;
        List<IItemStack> itemStackList = new ArrayList<>();
        for(ItemStack itemStack : itemStacks) {
            if(itemStack != null) {
                itemStackList.add(new ItemStackImpl(itemStack));
            }
        }
        return itemStackList;
    }

}

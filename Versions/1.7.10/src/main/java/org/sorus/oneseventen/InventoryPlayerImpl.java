

package org.sorus.oneseventen;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.sorus.client.version.game.IInventoryPlayer;
import org.sorus.client.version.game.IItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryPlayerImpl implements IInventoryPlayer {

    private final InventoryPlayer inventory;

    public InventoryPlayerImpl(InventoryPlayer inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<IItemStack> getArmor() {
        ItemStack[] itemStacks = this.inventory.armorInventory;
        List<IItemStack> itemStackList = new ArrayList<>();
        for(ItemStack itemStack : itemStacks) {
            if(itemStack != null) {
                itemStackList.add(new ItemStackImpl(itemStack));
            }
        }
        return itemStackList;
    }

}



package org.sorus.oneeightnine;

import net.minecraft.item.ItemStack;
import org.sorus.client.version.game.IItemStack;

import java.util.Objects;

public class ItemStackImpl implements IItemStack {

    private final ItemStack itemStack;

    public ItemStackImpl(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int getDamage() {
        return this.itemStack.getItemDamage();
    }

    @Override
    public int getMaxDamage() {
        return this.itemStack.getMaxDamage();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemStackImpl itemStack1 = (ItemStackImpl) o;
        return Objects.equals(itemStack, itemStack1.itemStack);
    }
}

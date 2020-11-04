/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

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

package org.sorus.oneeightnine;

import net.minecraft.item.Item;
import org.sorus.client.version.game.ArmorType;
import org.sorus.client.version.game.IItemManager;
import org.sorus.client.version.game.IItemStack;

public class ItemManager implements IItemManager {

    @Override
    public ArmorType getArmorType(IItemStack iItemStack) {
        ItemStackImpl itemStack = (ItemStackImpl) iItemStack;
        int id = Item.getIdFromItem(itemStack.getItemStack().getItem());
        switch(id) {
            case 298:
            case 302:
            case 306:
            case 310:
            case 314:
                return ArmorType.HELMET;
            case 299:
            case 303:
            case 307:
            case 311:
            case 315:
                return ArmorType.CHESTPLATE;
            case 300:
            case 304:
            case 308:
            case 312:
            case 316:
                return ArmorType.LEGGINGS;
            case 301:
            case 305:
            case 309:
            case 313:
            case 317:
                return ArmorType.BOOTS;
        }
        return null;
    }

}

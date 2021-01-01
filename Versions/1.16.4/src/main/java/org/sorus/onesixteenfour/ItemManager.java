

package org.sorus.onesixteenfour;

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

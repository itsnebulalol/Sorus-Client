

package org.sorus.oneeightnine.injectors.itemphysics;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.itemphysics.ItemPhysics;

public class RenderEntityItemHook {

    public static void performTranslations(Object object, double xTranslate, double yTranslate, float paramFloat1, double zTranslate, float velocity, float paramFloat3) {
        EntityItem entityItem = (EntityItem) object;
        if(Sorus.getSorus().getModuleManager().getModule(ItemPhysics.class).isEnabled()) {
            GlStateManager.translate(xTranslate, yTranslate + (0.01F * paramFloat1), zTranslate);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            if (!entityItem.onGround) {
                GlStateManager.rotate(velocity * 5000.0F, (float) entityItem.motionY, (float) entityItem.motionX, 0.0F);
            }
        } else {
            GlStateManager.translate(xTranslate, yTranslate + (0.25F * paramFloat1), zTranslate);
            float f = ((entityItem.getAge() + paramFloat3) / 20.0F + entityItem.hoverStart) * 57.295776F;
            GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
        }
    }

}

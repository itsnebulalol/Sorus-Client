

package org.sorus.oneseventen.injectors.itemphysics;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.itemphysics.ItemPhysics;

public class RenderItemHook {

    public static void performTranslations(Object object, float yBob) {
        EntityItem entityItem = (EntityItem) object;
        float velocity = MathHelper.sin(((float)entityItem.age + 0.11f) / 10.0F + entityItem.hoverStart) * 0.1F + 0.1F;
        if(Sorus.getSorus().getModuleManager().getModule(ItemPhysics.class).isEnabled()) {
            GL11.glTranslatef(0, -yBob, 0);
            GL11.glTranslated(0, -0.11, 0);
            GL11.glRotated(90, 1, 0, 0);
            if (!entityItem.onGround) {
                GL11.glRotated(velocity * 3000.0F, entityItem.motionY, entityItem.motionX, 0.0F);
            }
        }
    }

    public static float getRotate(float rotate) {
        return Sorus.getSorus().getModuleManager().getModule(ItemPhysics.class).isEnabled() ? 0 : rotate;
    }

}

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

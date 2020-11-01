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

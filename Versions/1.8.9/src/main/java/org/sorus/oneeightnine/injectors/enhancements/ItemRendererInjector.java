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

package org.sorus.oneeightnine.injectors.enhancements;

import net.minecraft.client.renderer.ItemRenderer;
import org.lwjgl.opengl.GL11;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.enhancements.Enhancements;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

@Hook("net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererInjector extends Injector<ItemRenderer> {

    public ItemRendererInjector(ItemRenderer that) {
        super(that);
    }

    private double customFireHeight;

    @Inject(name = "renderFireInFirstPerson", desc = "(F)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager;tryBlendFuncSeparate(IIII)V", shift = At.Shift.AFTER))
    public void renderFirstInFirstPersonHead() {
        Enhancements enhancements = Sorus.getSorus().getModuleManager().getModule(Enhancements.class);
        customFireHeight = enhancements.isEnabled() ? enhancements.getCustomFireHeight() : 0;
        GL11.glColor4d(1, 1, 1, enhancements.isEnabled() ? enhancements.getCustomFireOpacity() : 0.9);
        GL11.glTranslated(0, customFireHeight, 0);
    }

    @Inject(name = "renderFireInFirstPerson", desc = "(F)V", at = @At("RETURN"))
    public void renderFirstInFirstPersonReturn() {
        GL11.glTranslated(0, -customFireHeight, 0);
    }

}

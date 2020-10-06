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

package org.sorus.oneeightnine.injectors.blockoverlay;

import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.blockoverlay.BlockOverlay;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

import java.awt.*;

@Hook("net/minecraft/client/renderer/RenderGlobal")
public class RenderGlobalInjector extends Injector<RenderGlobal> {

    public RenderGlobalInjector(RenderGlobal that) {
        super(that);
    }

    @Inject(name = "drawSelectionBoundingBox", desc = "(Lnet/minecraft/util/AxisAlignedBB;)V", at = @At("HEAD"))
    public static void test() {
        BlockOverlay blockOverlay = Sorus.getSorus().getModuleManager().getModule(BlockOverlay.class);
        if(blockOverlay.isEnabled()) {
            Color color = blockOverlay.getColor();
            GL11.glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
            GL11.glLineWidth((float) blockOverlay.getBorderThickness());
        }
    }

}

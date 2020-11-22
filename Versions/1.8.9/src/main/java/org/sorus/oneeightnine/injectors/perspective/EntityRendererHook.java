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

package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.perspective.Perspective;
import org.sorus.client.util.MathUtil;

public class EntityRendererHook {

    public static float getRotationPitch() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationPitch() : Minecraft.getMinecraft().thePlayer.rotationPitch;
    }

    public static float getRotationYaw() {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        return perspective.isToggled() ? perspective.getRotationYaw() : Minecraft.getMinecraft().thePlayer.rotationYaw;
    }

    public static void setAngles(float yaw, float pitch) {
        Perspective perspective = Sorus.getSorus().getModuleManager().getModule(Perspective.class);
        if(!perspective.isToggled()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            player.setAngles(yaw, pitch);
            perspective.setRotationYaw(player.rotationYaw);
            perspective.setRotationPitch(player.rotationPitch);
        } else {
            perspective.setRotationYaw(perspective.getRotationYaw() + (yaw * 0.15F * (perspective.invertYaw() ? -1 : 1)));
            perspective.setRotationPitch(perspective.getRotationPitch() + (pitch * 0.15F * (perspective.invertYaw() ? -1 : 1)));
        }
    }

}

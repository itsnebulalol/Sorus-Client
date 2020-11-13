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

package org.sorus.oneeightnine.injectors.oldanimations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.oldanimations.OldAnimations;

public class MinecraftHook {

    public static void runTick() {
        if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Sorus.getSorus().getModuleManager().getModule(OldAnimations.class).shouldShowAnimInteractBreak()) {
            MinecraftHook.attemptSwing();
        }
    }

    private static void attemptSwing() {
        if (Minecraft.getMinecraft().thePlayer.getItemInUseCount() > 0) {
            final boolean mouseDown = Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown() && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                MinecraftHook.swingItem(Minecraft.getMinecraft().thePlayer);
            }
        }
    }

    private static void swingItem(Object object) {
        EntityPlayerSP entityPlayerSP = (EntityPlayerSP) object;
        final int swingAnimationEnd = entityPlayerSP.isPotionActive(Potion.digSpeed) ? (6 - (1 + entityPlayerSP.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (entityPlayerSP.isPotionActive(Potion.digSlowdown) ? (6 + (1 + entityPlayerSP.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!entityPlayerSP.isSwingInProgress || entityPlayerSP.swingProgressInt >= swingAnimationEnd / 2 || entityPlayerSP.swingProgressInt < 0) {
            entityPlayerSP.swingProgressInt = -1;
            entityPlayerSP.isSwingInProgress = true;
        }
    }

}

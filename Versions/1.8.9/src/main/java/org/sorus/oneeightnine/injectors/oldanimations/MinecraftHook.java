

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

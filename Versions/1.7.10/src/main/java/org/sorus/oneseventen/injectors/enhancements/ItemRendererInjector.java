

package org.sorus.oneseventen.injectors.enhancements;

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

    @Inject(name = "renderFireInFirstPerson", desc = "(F)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/OpenGlHelper;glBlendFunc(IIII)V", shift = At.Shift.AFTER))
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

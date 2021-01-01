

package org.sorus.oneeightnine.injectors;

import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.render.Render2DEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;


@Hook("net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererInjector extends Injector<EntityRenderer> {

    public EntityRendererInjector(EntityRenderer that) {
        super(that);
    }


    @Inject(name = "updateCameraAndRender", desc = "(FJ)V", at = @At(value = "RETURN"))
    public void updateCameraAndRender() {
        Sorus.getSorus().getEventManager().post(new Render2DEvent());
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}

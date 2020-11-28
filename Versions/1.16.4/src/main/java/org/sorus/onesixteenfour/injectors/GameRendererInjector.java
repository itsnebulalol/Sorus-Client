package org.sorus.onesixteenfour.injectors;

import net.minecraft.client.renderer.GameRenderer;
import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.render.Render2DEvent;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

@Hook("net/minecraft/client/renderer/GameRenderer")
public class GameRendererInjector extends Injector<GameRenderer> {

    public GameRendererInjector(GameRenderer that) {
        super(that);
    }

    @Inject(name = "updateCameraAndRender", desc = "(FJZ)V", at = @At("RETURN"))
    public static void onUpdateCameraAndRender() {
        Sorus.getSorus().getEventManager().post(new Render2DEvent());
    }

}

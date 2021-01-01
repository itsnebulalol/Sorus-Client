package org.sorus.oneeightnine.injectors;

import net.minecraft.entity.Entity;
import org.sorus.client.Sorus;
import org.sorus.client.event.impl.client.render.RenderEntityEvent;
import org.sorus.oneeightnine.util.Util;

public class RenderManagerHook {

    public static void onRenderEntity(Entity entity) {
        Sorus.getSorus().getEventManager().post(new RenderEntityEvent(Util.getEntity(entity)));
    }

}

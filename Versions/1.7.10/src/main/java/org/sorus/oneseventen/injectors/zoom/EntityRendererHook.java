package org.sorus.oneseventen.injectors.zoom;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.zoom.Zoom;

public class EntityRendererHook {

    public static float getFOV(float initalFOV) {
        Zoom zoom = Sorus.getSorus().getModuleManager().getModule(Zoom.class);
        if(!zoom.isToggled()) {
            return initalFOV;
        }
        return zoom.getFOV();
    }

}

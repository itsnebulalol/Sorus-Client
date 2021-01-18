package org.sorus.oneeightnine.injectors.zoom;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.zoom.Zoom;

public class InventoryPlayerHook {

    public static int getCurrentItemChange(int original) {
        if(Sorus.getSorus().getModuleManager().getModule(Zoom.class).isToggled()) {
            return 0;
        }
        return original;
    }

}



package org.sorus.oneeightnine.injectors.enhancements;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.enhancements.Enhancements;

public class EntityRendererHook {

    public static float getBrightness(float initial) {
        return Sorus.getSorus().getModuleManager().getModule(Enhancements.class).fullBright() ? 100 : initial;
    }

}

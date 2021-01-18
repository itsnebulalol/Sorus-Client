package org.sorus.oneeightnine.injectors.particles;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.particlemultiplier.Particles;

public class EntityPlayerHook {

    public static float getValue(float initial) {
        return initial > 0 ? initial : Sorus.getSorus().getModuleManager().getModule(Particles.class).isAlwaysSharp() ? 0.1f : 0;
    }

    public static boolean showCriticals(boolean initial) {
        return initial || Sorus.getSorus().getModuleManager().getModule(Particles.class).isAlwaysParticles();
    }

}

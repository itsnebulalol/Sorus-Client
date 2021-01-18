package org.sorus.oneseventen.injectors.particles;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.particlemultiplier.Particles;

public class EntityCrit2FXHook {

    public static int getParticles() {
        return (int) Math.round(Sorus.getSorus().getModuleManager().getModule(Particles.class).getMultiplier() * 16);
    }

}

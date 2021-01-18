package org.sorus.oneeightnine.injectors.particles;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.particlemultiplier.Particles;

public class EntityParticleEmitterHook {

    public static int getParticles() {
        return (int) Math.round(Sorus.getSorus().getModuleManager().getModule(Particles.class).getMultiplier() * 16);
    }

}

package org.sorus.onesixteenfour.injectors;

import net.minecraft.client.Minecraft;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;

@Hook("net/minecract/client/Minecraft")
public class MinecraftInjector extends Injector<Minecraft> {

    public MinecraftInjector(Minecraft that) {
        super(that);
    }
}

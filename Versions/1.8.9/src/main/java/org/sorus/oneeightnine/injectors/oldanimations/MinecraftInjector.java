

package org.sorus.oneeightnine.injectors.oldanimations;
import net.minecraft.client.Minecraft;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

@Hook("net/minecraft/client/Minecraft")
public class MinecraftInjector extends Injector<Minecraft> {

    public MinecraftInjector(Minecraft that) {
        super(that);
    }

    @Inject(name = "runTick", at = @At("HEAD"))
    public void runTick() {
        MinecraftHook.runTick();
    }

}

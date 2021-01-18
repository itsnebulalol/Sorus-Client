package org.sorus.client.module;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.blockoverlay.BlockOverlay;
import org.sorus.client.module.impl.challenges.Challenges;
import org.sorus.client.module.impl.chatmacros.ChatMacros;
import org.sorus.client.module.impl.crosshair.Crosshair;
import org.sorus.client.module.impl.customscreens.CustomMenus;
import org.sorus.client.module.impl.discordrp.DiscordRP;
import org.sorus.client.module.impl.enhancements.Enhancements;
import org.sorus.client.module.impl.itemphysics.ItemPhysics;
import org.sorus.client.module.impl.mousedelayfix.MouseDelayFix;
import org.sorus.client.module.impl.names.Names;
import org.sorus.client.module.impl.oldanimations.OldAnimations;
import org.sorus.client.module.impl.particlemultiplier.Particles;
import org.sorus.client.module.impl.perspective.Perspective;
import org.sorus.client.module.impl.timechanger.TimeChanger;
import org.sorus.client.module.impl.togglesprint.ToggleSprint;
import org.sorus.client.module.impl.zoom.Zoom;

public class ModuleManager {

  private final List<Module> modules = new ArrayList<>();

  public void register(Module module) {
    if (module.getVersions().allow(Sorus.getSorus().getArgs().get("version"))) {
      this.modules.add(module);
    }
  }

  public void registerInternalModules() {
    this.register(new BlockOverlay());
    this.register(new Challenges());
    this.register(new ChatMacros());
    this.register(new Crosshair());
    this.register(new Names());
    this.register(new DiscordRP());
    this.register(new Enhancements());
    this.register(new ItemPhysics());
    this.register(new MouseDelayFix());
    // this.register(new Music());
    this.register(new OldAnimations());
    this.register(new Particles());
    this.register(new Perspective());
    this.register(new CustomMenus());
    this.register(new TimeChanger());
    this.register(new ToggleSprint());
    this.register(new Zoom());
  }

  public void onLoad() {
    modules.forEach(Module::onLoad);
  }

  public <T extends Module> T getModule(Class<T> moduleClass) {
    for (Module module : this.getModules()) {
      if (moduleClass.isAssignableFrom(module.getClass())) {
        return (T) module;
      }
    }
    return null;
  }

  public List<Module> getModules() {
    return modules;
  }

  public <T extends Module> List<T> getModules(Class<T> clazz) {
    List<T> modules = new ArrayList<>();
    for (Module module : this.getModules()) {
      if (clazz.isAssignableFrom(module.getClass())) {
        modules.add((T) module);
      }
    }
    return modules;
  }
}

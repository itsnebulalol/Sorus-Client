/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.module;

import java.util.ArrayList;
import java.util.List;

import org.sorus.client.Sorus;
import org.sorus.client.module.impl.blockoverlay.BlockOverlay;
import org.sorus.client.module.impl.challenges.Challenges;
import org.sorus.client.module.impl.chatmacros.ChatMacros;
import org.sorus.client.module.impl.crosshair.Crosshair;
import org.sorus.client.module.impl.customscreens.SorusMainMenuVisibler;
import org.sorus.client.module.impl.discordrp.DiscordRP;
import org.sorus.client.module.impl.enhancements.Enhancements;
import org.sorus.client.module.impl.fullbright.Fullbright;
import org.sorus.client.module.impl.itemphysics.ItemPhysics;
import org.sorus.client.module.impl.mousedelayfix.MouseDelayFix;
import org.sorus.client.module.impl.music.Music;
import org.sorus.client.module.impl.oldanimations.OldAnimations;
import org.sorus.client.module.impl.perspective.Perspective;
import org.sorus.client.module.impl.timechanger.TimeChanger;
import org.sorus.client.module.impl.togglesprint.ToggleSprint;

/** Keeps track of and handles all the current modules that it has been registered with. */
public class ModuleManager {

  /** List of the current loaded modules */
  private final List<Module> modules;

  public ModuleManager() {
    this.modules = new ArrayList<>();
  }

  /**
   * Registers a modules by adding it to the list of registered modules.
   *
   * @param module module to register
   */
  public void register(Module module) {
    if(module.getVersions().allow(Sorus.getSorus().getArgs().get("version"))) {
      this.modules.add(module);
    }
  }

  /** Handles registering all the modules that come by default with Sorus. */
  public void registerInternalModules() {
    this.register(new BlockOverlay());
    this.register(new Challenges());
    this.register(new ChatMacros());
    this.register(new Crosshair());
    this.register(new DiscordRP());
    this.register(new Enhancements());
    this.register(new Fullbright());
    this.register(new ItemPhysics());
    this.register(new MouseDelayFix());
    this.register(new Music());
    this.register(new OldAnimations());
    this.register(new Perspective());
    this.register(new SorusMainMenuVisibler());
    this.register(new TimeChanger());
    this.register(new ToggleSprint());
  }

  /** Goes through all the modules and enables them. */
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

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

package org.sorus.client;

import java.util.Map;
import org.sorus.client.event.EventManager;
import org.sorus.client.gui.core.GUIManager;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.HUDRenderScreen;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.module.ModuleManager;
import org.sorus.client.plugin.PluginManager;
import org.sorus.client.settings.SettingsManager;
import org.sorus.client.version.IVersion;

public class Sorus {

  /** The single {@link Sorus} instance */
  private static final Sorus INSTANCE = new Sorus();

  public static Sorus getSorus() {
    return INSTANCE;
  }

  private Map<String, String> args;

  /** The {@link EventManager} for Sorus */
  private final EventManager EVENT_MANAGER = new EventManager();

  /** The {@link ModuleManager} for Sorus */
  private final ModuleManager MODULE_MANAGER = new ModuleManager();

  /** The {@link GUIManager} for Sorus */
  private final GUIManager GUI_MANAGER = new GUIManager();

  /** The {@link HUDManager} for Sorus */
  private final HUDManager HUD_MANAGER = new HUDManager();

  /** The {@link SettingsManager} for Sorus */
  private final SettingsManager SETTINGS_MANAGER = new SettingsManager();

  /** The {@link PluginManager} for Sorus */
  private final PluginManager PLUGIN_MANAGER = new PluginManager();

  /** The {@link ThemeManager} for Sorus */
  private final ThemeManager THEME_MANAGER = new ThemeManager();

  /** The {@link IVersion} for Sorus */
  private IVersion version;

  private Sorus() {}

  /**
   * Called when the client is first launched. Initializes important processes for the client to
   * operate.
   *
   * @param version the version class used to connect with the version of minecraft
   */
  public void initialize(Class<? extends IVersion> version, Map<String, String> args) {
    try {
      this.version = version.newInstance();
      Class.forName("org.sorus.client.obfuscation.ObfuscationManager");
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    this.args = args;
    this.getModuleManager().registerInternalModules();
    this.getModuleManager().onLoad();
    this.getGUIManager().initialize(this.getThemeManager());
    this.getHUDManager().initialize();
    this.getGUIManager().open(new HUDRenderScreen(this.getHUDManager()));
    this.getPluginManager().initialize(args.get("plugins"));
    this.getSettingsManager().load();
  }

  public Map<String, String> getArgs() {
    return args;
  }

  public EventManager getEventManager() {
    return EVENT_MANAGER;
  }

  public ModuleManager getModuleManager() {
    return MODULE_MANAGER;
  }

  public GUIManager getGUIManager() {
    return GUI_MANAGER;
  }

  public HUDManager getHUDManager() {
    return HUD_MANAGER;
  }

  public SettingsManager getSettingsManager() {
    return SETTINGS_MANAGER;
  }

  public PluginManager getPluginManager() {
    return PLUGIN_MANAGER;
  }

  public ThemeManager getThemeManager() {
    return THEME_MANAGER;
  }

  public IVersion getVersion() {
    return version;
  }
}

package org.sorus.client;

import java.io.InputStream;
import java.util.Map;
import org.sorus.client.cosmetic.CosmeticManager;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.EventManager;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.GUIManager;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.HUDRenderScreen;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.module.ModuleManager;
import org.sorus.client.plugin.PluginManager;
import org.sorus.client.settings.SettingsManager;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.IVersion;
import org.sorus.client.version.game.IGame;

public class Sorus {

  public static boolean isRunning = false;

  private static final Sorus INSTANCE = new Sorus();

  public static Sorus getSorus() {
    return INSTANCE;
  }

  private Map<String, String> args;

  private final EventManager EVENT_MANAGER = new EventManager();

  private final ModuleManager MODULE_MANAGER = new ModuleManager();

  private final GUIManager GUI_MANAGER = new GUIManager();

  private final HUDManager HUD_MANAGER = new HUDManager();

  private final SettingsManager SETTINGS_MANAGER = new SettingsManager();

  private final PluginManager PLUGIN_MANAGER = new PluginManager();

  private final ThemeManager THEME_MANAGER = new ThemeManager();

  private final CosmeticManager COSMETIC_MANAGER = new CosmeticManager();

  private IVersion version;

  private Sorus() {}

  public void initialize(Class<? extends IVersion> version, Map<String, String> args) {
    isRunning = true;
    try {
      this.version = version.newInstance();
      Class.forName("org.sorus.client.obfuscation.ObfuscationManager");
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    this.args = args;
    this.getModuleManager().registerInternalModules();
    this.getGUIManager().initialize();
    this.getHUDManager().initialize();
    this.getGUIManager().open(new HUDRenderScreen());
    this.getPluginManager().initialize(args.get("plugins"));
    this.getSettingsManager().load("default");
    this.getModuleManager().onLoad();
    this.getCosmeticManager().onLoad();
    this.getEventManager().register(this);
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

  public CosmeticManager getCosmeticManager() {
    return COSMETIC_MANAGER;
  }

  public IVersion getVersion() {
    return version;
  }

  @EventInvoked
  public void configureWindow(StartEvent e) {
    InputStream icon =
        Sorus.class.getClassLoader().getResourceAsStream("assets/minecraft/sorus/sorus_icon.png");
    this.getVersion().getData(IScreen.class).setIcon(icon);
    this.getVersion().getData(IScreen.class).setTitle("Sorus Client");
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (Sorus.getSorus().getVersion().getData(IGame.class).isIngame()
        && e.getKey() == this.getSettingsManager().getOpenGuiKeybind()
        && !e.isRepeat()) {
      Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(true));
    }
  }
}

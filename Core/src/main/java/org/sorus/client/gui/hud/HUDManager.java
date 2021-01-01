package org.sorus.client.gui.hud;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.module.impl.armorstatus.ArmorStatusComponent;
import org.sorus.client.module.impl.clock.ClockComponent;
import org.sorus.client.module.impl.cps.CPSComponent;
import org.sorus.client.module.impl.fps.FPSComponent;
import org.sorus.client.module.impl.gps.GPSComponent;
import org.sorus.client.module.impl.keystrokes.KeystrokesComponent;
import org.sorus.client.module.impl.ping.PingComponent;
import org.sorus.client.module.impl.potionstatus.PotionStatusComponent;
import org.sorus.client.module.impl.scoreboard.ScoreboardComponent;
import org.sorus.client.module.impl.text.TextComponent;
import org.sorus.client.settings.ISettingHolder;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.Key;

public class HUDManager implements ISettingHolder {

  private final List<Class<? extends Component>> registeredComponents;

  private final List<HUD> huds;

  public HUDManager() {
    this.registeredComponents = new ArrayList<>();
    this.huds = new ArrayList<>();
  }

  public void initialize() {
    Sorus.getSorus().getEventManager().register(this);
    this.registerInternalComponents();
    Sorus.getSorus().getSettingsManager().register(this);
  }

  public void registerInternalComponents() {
    this.register(ArmorStatusComponent.class);
    // this.registerComponent(ChatComponent.class);
    this.register(ClockComponent.class);
    this.register(CPSComponent.class);
    this.register(FPSComponent.class);
    this.register(GPSComponent.class);
    this.register(KeystrokesComponent.class);
    this.register(PingComponent.class);
    this.register(PotionStatusComponent.class);
    this.register(ScoreboardComponent.class);
    this.register(TextComponent.class);
  }

  public void register(Class<? extends Component> component) {
    this.registeredComponents.add(component);
  }

  public void register(HUD hud) {
    this.huds.add(hud);
  }

  public void unregister(HUD hud) {
    this.huds.remove(hud);
    for (IComponent component : hud.getHUDComponents()) {
      component.onRemove();
    }
  }

  public List<HUD> getHUDs() {
    return huds;
  }

  public List<Class<? extends Component>> getRegisteredComponents() {
    return registeredComponents;
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (Sorus.getSorus().getVersion().getData(IGame.class).isIngame()
        && e.getKey() == Key.SHIFT_RIGHT
        && !e.isRepeat()) {
      Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(true));
    }
  }

  @Override
  public List<Pair<Class<? extends HUD>, ?>> getSettings() {
    List<Pair<Class<? extends HUD>, ?>> settings = new ArrayList<>();
    for (HUD hud : this.huds) {
      settings.add(Pair.of(hud.getClass(), hud.getSettings()));
    }
    return settings;
  }

  @Override
  public void setSettings(Object settings) {
    this.huds.clear();
    List<Pair<Class<? extends HUD>, ?>> settingsList =
        (List<Pair<Class<? extends HUD>, ?>>) settings;
    for (Pair<Class<? extends HUD>, ?> pair : settingsList) {
      try {
        HUD hud = pair.getKey().newInstance();
        hud.setSettings(pair.getValue());
        this.register(hud);
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getSettingsName() {
    return "hud";
  }
}

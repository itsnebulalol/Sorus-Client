package org.sorus.client.module.impl.zoom;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.KeyReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class Zoom extends ModuleConfigurable {

  private final Setting<Input> keybind;
  private final Setting<Double> defaultZoomFOV;
  private final Setting<Double> minZoomFOV;
  private final Setting<Double> maxZoomFOV;

  private boolean toggled;

  private double targetFOV;
  private double currentFOV;

  public Zoom() {
    super("Zoom");
    this.register(keybind = new Setting<>("keybind", Key.C));
    this.register(defaultZoomFOV = new Setting<>("defaultZoomFOV", 30.));
    this.register(minZoomFOV = new Setting<>("minZoomFOV", 15.));
    this.register(maxZoomFOV = new Setting<>("maxZoomFOV", 45.));
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (!e.isRepeat() && e.getKey() == keybind.getValue()) {
      toggled = true;
      Sorus.getSorus().getVersion().getData(IGame.class).setSmoothCamera(true);
      targetFOV = 0;
      currentFOV = 0;
    }
  }

  @EventInvoked
  public void onKeyRelease(KeyReleaseEvent e) {
    if (e.getKey() == keybind.getValue()) {
      toggled = false;
      Sorus.getSorus().getVersion().getData(IGame.class).setSmoothCamera(false);
    }
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    targetFOV -= Sorus.getSorus().getVersion().getData(IInput.class).getScroll() / 1000;
    targetFOV = MathUtil.clamp(targetFOV, -1, 1);
    currentFOV = (targetFOV + currentFOV) / 2;
  }

  public boolean isToggled() {
    return this.isEnabled() && toggled;
  }

  public float getFOV() {
    return currentFOV >= 0
        ? (float) MathUtil.getBetween(defaultZoomFOV.getValue(), maxZoomFOV.getValue(), currentFOV)
        : (float)
            MathUtil.getBetween(minZoomFOV.getValue(), defaultZoomFOV.getValue(), 1 + currentFOV);
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(keybind, "Keybind"));
    collection.add(new Slider(defaultZoomFOV, 15, 40, "Default Zoom FOV"));
    collection.add(new Slider(minZoomFOV, 5, 30, "Minimum Zoom FOV"));
    collection.add(new Slider(maxZoomFOV, 25, 50, "Maximum Zoom FOV"));
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

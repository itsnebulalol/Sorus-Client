package org.sorus.client.module.impl.perspective;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.KeyReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.game.PerspectiveMode;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class Perspective extends ModuleConfigurable {

  private final Setting<Input> keybind;
  private final Setting<Boolean> invertYaw;
  private final Setting<Boolean> invertPitch;

  private boolean toggled;
  private PerspectiveMode savedPerspective;
  private float rotationPitch, rotationYaw;

  public Perspective() {
    super("Perspective");
    this.register(keybind = new Setting<>("keybind", Key.F));
    this.register(invertYaw = new Setting<>("invertYaw", false));
    this.register(invertPitch = new Setting<>("invertPitch", false));
  }

  @Override
  public void onLoad() {
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (this.isEnabled()
        && e.getKey().equals(keybind.getValue())
        && !toggled
        && Sorus.getSorus().getVersion().getData(IGame.class).isIngame()) {
      toggled = true;
      savedPerspective = Sorus.getSorus().getVersion().getData(IGame.class).getPerspective();
      Sorus.getSorus()
          .getVersion()
          .getData(IGame.class)
          .setPerspective(PerspectiveMode.THIRD_PERSON_BACK);
      if (savedPerspective == PerspectiveMode.THIRD_PERSON_FRONT) {
        rotationYaw += 180;
        rotationPitch = -rotationPitch;
      }
    }
  }

  @EventInvoked
  public void onKeyRelease(KeyReleaseEvent e) {
    if (this.isEnabled() && e.getKey().equals(keybind.getValue()) && toggled) {
      toggled = false;
      Sorus.getSorus().getVersion().getData(IGame.class).setPerspective(savedPerspective);
    }
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(keybind, "Perspective Toggle"));
    collection.add(new Toggle(invertYaw, "Invert Yaw"));
    collection.add(new Toggle(invertPitch, "Invert Pitch"));
  }

  @Override
  public String getDescription() {
    return "Look around your character like in F5 without turning your character.";
  }

  public boolean isToggled() {
    return this.isEnabled() && toggled;
  }

  public float getRotationPitch() {
    return rotationPitch;
  }

  public float getRotationYaw() {
    return rotationYaw;
  }

  public void setRotationPitch(float rotationPitch) {
    this.rotationPitch = rotationPitch;
  }

  public void setRotationYaw(float rotationYaw) {
    this.rotationYaw = rotationYaw;
  }

  public boolean invertPitch() {
    return this.invertPitch.getValue();
  }

  public boolean invertYaw() {
    return this.invertYaw.getValue();
  }
}

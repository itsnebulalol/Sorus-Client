package org.sorus.client.module.impl.togglesprint;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.input.KeybindType;

public class ToggleSprint extends ModuleConfigurable {

  private final Setting<Input> toggleSprint;

  private boolean toggled;

  public ToggleSprint() {
    super("Toggle Sprint");
    this.register(toggleSprint = new Setting<>("toggleSprint", Key.R));
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
        && Sorus.getSorus().getVersion().getData(IGame.class).isIngame()
        && e.getKey().equals(toggleSprint.getValue())) {
      toggled = !toggled;
      Sorus.getSorus()
          .getVersion()
          .getData(IInput.class)
          .getKeybind(KeybindType.SPRINT)
          .setState(toggled);
    }
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    if (this.isEnabled()
        && this.toggled
        && Sorus.getSorus().getVersion().getData(IGame.class).isIngame()) {
      Sorus.getSorus()
          .getVersion()
          .getData(IInput.class)
          .getKeybind(KeybindType.SPRINT)
          .setState(true);
    }
  }

  @Override
  public String getDescription() {
    return "Allows toggling... sprint. Pretty self explanatory.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(toggleSprint, "Toggle Sprint"));
  }
}

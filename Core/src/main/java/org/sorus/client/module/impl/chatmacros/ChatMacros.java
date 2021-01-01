package org.sorus.client.module.impl.chatmacros;

import java.util.LinkedHashMap;
import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class ChatMacros extends ModuleConfigurable {

  private final Setting<Map<Input, String>> macros;

  public ChatMacros() {
    super("Chat Macros");
    this.register(macros = new Setting<>("macros", new LinkedHashMap<>()));
    this.macros.getValue().put(Key.U, "Hello World!");
    this.macros.getValue().put(Key.Y, "Hello World2!");
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (!Sorus.getSorus().getVersion().getData(IGame.class).isIngame() || !this.isEnabled()) {
      return;
    }
    String string = macros.getValue().get(e.getKey());
    if (string != null) {
      Sorus.getSorus().getVersion().getData(IGame.class).sendChatMessage(string);
    }
  }

  @Override
  public String getDescription() {
    return "Quickly send chat messages with a press of a button.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new MacrosSetting(macros));
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

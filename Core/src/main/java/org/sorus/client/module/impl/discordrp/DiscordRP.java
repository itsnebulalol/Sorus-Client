package org.sorus.client.module.impl.discordrp;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.time.OffsetDateTime;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;

public class DiscordRP extends ModuleConfigurable {

  private final IPCClient client = new IPCClient(763060164126834729L);
  private final RichPresence.Builder builder = new RichPresence.Builder();
  private boolean connected;

  private final Setting<String> status;

  private String details = "";

  public DiscordRP() {
    super("Discord RPC");
    this.register(status = new Setting<>("status", ""));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onLoad() {
    builder.setStartTimestamp(OffsetDateTime.now()).setLargeImage("logo_dark", "Sorus Client");
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  if (connected) {
                    client.close();
                  }
                }));
    super.onLoad();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public void onEnable() {
    if (!connected) {
      new Thread(
              () -> {
                try {
                  client.connect();
                  client.sendRichPresence(builder.build());
                  connected = true;
                } catch (NoDiscordClientException e) {
                  e.printStackTrace();
                }
              })
          .start();
    }
  }

  @Override
  public void onDisable() {
    if (connected) {
      client.close();
      connected = false;
    }
  }

  @EventInvoked
  public void onGuiSwitch(GuiSwitchEvent e) {
    if (Sorus.getSorus().getVersion().getData(IGame.class).getPlayer().exists()) {
      String serverIP = Sorus.getSorus().getVersion().getData(IGame.class).getCurrentServerIP();
      if (serverIP == null) {
        this.updateState("Playing Singleplayer");
      } else {
        this.updateState("Playing " + serverIP);
      }
    } else {
      this.updateState("In Menus");
    }
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    if (!status.getValue().equals(details)) {
      details = status.getValue();
      this.updateDetails(details);
    }
  }

  public void updateState(String state) {
    builder.setState(state);
    this.update();
  }

  public void updateDetails(String details) {
    builder.setDetails(details);
    this.update();
  }

  private void update() {
    if (connected) {
      client.sendRichPresence(builder.build());
    }
  }

  @Override
  public String getDescription() {
    return "Show all your friends that your playing Sorus!";
  }
}

package org.sorus.client.module.impl.discordrp;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.time.OffsetDateTime;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.module.ModuleConfigurable;

public class DiscordRP extends ModuleConfigurable {

  private final IPCClient client = new IPCClient(763060164126834729L);
  private final RichPresence.Builder builder = new RichPresence.Builder();

  public DiscordRP() {
    super("DISCORD RPC");
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onLoad() {
    client.setListener(new IPCListener() {});
    builder.setStartTimestamp(OffsetDateTime.now()).setLargeImage("logo_dark", "Sorus Client");
    Runtime.getRuntime().addShutdownHook(new Thread(client::close));
  }

  @Override
  public void onEnable() {
    try {
      client.connect();
      client.sendRichPresence(builder.build());
    } catch (NoDiscordClientException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDisable() {
    client.close();
  }

  @EventInvoked
  public void onGuiSwitch(GuiSwitchEvent e) {
    if (!Sorus.getSorus().getVersion().getGame().getPlayer().isNull()) {
      String serverIP = Sorus.getSorus().getVersion().getGame().getCurrentServerIP();
      if (serverIP == null) {
        this.setPresence("Playing Singleplayer");
      } else {
        this.setPresence("Playing " + serverIP);
      }
    } else {
      this.setPresence("In Menus");
    }
  }

  public void setPresence(String firstLine) {
    builder.setDetails(firstLine);
    if(this.isEnabled()) {
      client.sendRichPresence(builder.build());
    }
  }
}

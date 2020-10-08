package org.sorus.client.module.impl.discordrp;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.module.ModuleConfigurable;

public class DiscordRP extends ModuleConfigurable {

  private final DiscordRichPresence presence = new DiscordRichPresence();
  private DiscordEventHandlers handlers;

  public DiscordRP() {
    super("DISCORD RPC");
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onLoad() {
    handlers = new DiscordEventHandlers.Builder().build();
    presence.startTimestamp = System.currentTimeMillis();
    presence.largeImageKey = "main";
    Runtime.getRuntime().addShutdownHook(new Thread(DiscordRPC::discordShutdown));
  }

  @Override
  public void onEnable() {
    DiscordRPC.discordInitialize("763060164126834729", handlers, true);
  }

  @Override
  public void onDisable() {
    DiscordRPC.discordShutdown();
  }

  @EventInvoked
  public void onStart(StartEvent e) {
    if (this.isEnabled()) {
      this.onEnable();
    }
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    if (this.isEnabled()) {
      DiscordRPC.discordRunCallbacks();
    }
  }

  @EventInvoked
  public void onGuiSwitch(GuiSwitchEvent e) {
    switch(e.getType()) {
      case NULL:
        String serverIP = Sorus.getSorus().getVersion().getGame().getCurrentServerIP();
        if(serverIP == null) {
          this.setPresence("Playing Singleplayer");
        } else {
          this.setPresence("Playing " + serverIP);
        }
        break;
      case MAIN_MENU:
        this.setPresence("In Menus");
        break;
    }
  }

  public void setPresence(String firstLine) {
    presence.details = firstLine;
    DiscordRPC.discordUpdatePresence(presence);
  }
}

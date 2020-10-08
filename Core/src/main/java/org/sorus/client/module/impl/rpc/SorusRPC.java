package org.sorus.client.module.impl.rpc;


import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.sorus.client.Sorus;

public class SorusRPC {

    DiscordRichPresence presence = new DiscordRichPresence();

    public void init() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
            System.out.println("Welcome " + user.username + "#" + user.discriminator + " to Sorus Client!");
        }).build();

        presence.startTimestamp = System.currentTimeMillis() / 1000;

        setPresence("Playing Sorus", "I'll add server ip later");

        DiscordRPC.discordInitialize("763060164126834729", handlers, true);
        new Thread(() -> {
            while(true) {
                DiscordRPC.discordRunCallbacks();
            }
        }).start();
    }

    public void setPresence(String firstLine, String secondLine) {
        presence.details = firstLine;
        presence.state = secondLine;
        DiscordRPC.discordUpdatePresence(presence);
    }

//    public void setPresenceWithServerIpOnSecondLine(String firstLine) {
//        presence.details = firstLine;
//        presence.state = Sorus.getSorus().getVersion().getGame().getCurrentServerIP();
//        DiscordRPC.discordUpdatePresence(presence);
//    }

    public void resetPresence() {
        setPresence("Playing Sorus", "");
        DiscordRPC.discordUpdatePresence(presence);
    }

    public void shutdown() {
        DiscordRPC.discordShutdown();
    }

}

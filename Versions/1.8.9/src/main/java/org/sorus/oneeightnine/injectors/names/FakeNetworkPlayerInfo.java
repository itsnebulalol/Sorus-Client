

package org.sorus.oneeightnine.injectors.names;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class FakeNetworkPlayerInfo extends NetworkPlayerInfo {

    private final NetworkPlayerInfo original;
    private final String name;
    private final boolean override;

    public FakeNetworkPlayerInfo(NetworkPlayerInfo original, String name, boolean override) {
        super(original.getGameProfile());
        this.original = original;
        this.name = name;
        this.override = override;
    }

    @Override
    public IChatComponent getDisplayName() {
        if(override) {
            return new ChatComponentText(name);
        }
        String defaultName = original.getDisplayName() != null ? original.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(original.getPlayerTeam(), original.getGameProfile().getName());
        return new ChatComponentText(defaultName.replace(original.getGameProfile().getName(), name));
    }

    @Override
    public ScorePlayerTeam getPlayerTeam() {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(super.getGameProfile().getName());
    }

    @Override
    public ResourceLocation getLocationSkin() {
        return original.getLocationSkin();
    }

    @Override
    public com.mojang.authlib.GameProfile getGameProfile() {
        return new GameProfile(this.name);
    }

    public static class GameProfile extends com.mojang.authlib.GameProfile {

        public GameProfile(String name) {
            super(UUID.randomUUID(), name);
        }

        @Override
        public String getName() {
            return super.getName();
        }

    }

}

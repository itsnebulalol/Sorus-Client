package org.sorus.oneeightnine;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import org.sorus.client.version.game.IPlayer;
import org.sorus.client.version.game.IWorld;

import java.util.ArrayList;
import java.util.List;

public class WorldImpl implements IWorld {

    private final WorldClient world;

    public WorldImpl(WorldClient world) {
        this.world = world;
    }

    @Override
    public List<IPlayer> getPlayers() {
        List<EntityPlayer> worldPlayers = world.playerEntities;
        List<IPlayer> players = new ArrayList<>();
        for(EntityPlayer player : worldPlayers) {
            players.add(new PlayerImpl(player));
        }
        return players;
    }

}

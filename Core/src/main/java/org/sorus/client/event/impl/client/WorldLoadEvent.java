package org.sorus.client.event.impl.client;

import org.sorus.client.event.Event;
import org.sorus.client.version.game.IWorld;

public class WorldLoadEvent extends Event {

  private final IWorld world;

  public WorldLoadEvent(IWorld world) {
    this.world = world;
  }

  public IWorld getWorld() {
    return world;
  }
}

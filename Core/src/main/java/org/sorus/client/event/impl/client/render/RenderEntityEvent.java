package org.sorus.client.event.impl.client.render;

import org.sorus.client.event.Event;
import org.sorus.client.version.game.IEntity;

public class RenderEntityEvent extends Event {

  private final IEntity entity;

  public RenderEntityEvent(IEntity entity) {
    this.entity = entity;
  }

  public IEntity getEntity() {
    return entity;
  }
}

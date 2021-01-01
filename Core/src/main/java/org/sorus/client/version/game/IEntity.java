package org.sorus.client.version.game;

public interface IEntity {

  double getX();

  double getY();

  double getZ();

  boolean exists();

  EntityType getType();
}

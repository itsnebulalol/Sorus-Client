package org.sorus.client.version.game;

public interface IScoreboard {

  IScoreObjective getObjectiveInSlot(int slot);

  IScoreObjective getDummyObjective();
}

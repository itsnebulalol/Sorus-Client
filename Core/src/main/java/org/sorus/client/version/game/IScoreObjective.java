package org.sorus.client.version.game;

import java.util.List;

public interface IScoreObjective {

  boolean exists();

  String getName();

  List<IScore> getScores();
}



package org.sorus.oneeightnine;

import org.sorus.client.version.game.IScore;
import org.sorus.client.version.game.IScoreObjective;

import java.util.ArrayList;
import java.util.List;

public class DummyScoreboardObjective implements IScoreObjective {

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public String getName() {
        return "Sorus Client";
    }

    @Override
    public List<IScore> getScores() {
        List<IScore> scores = new ArrayList<>();
        scores.add(new Score("", 15));
        scores.add(new Score("Discord: §6jUnkASC", 14));
        return scores;
    }

    public static class Score implements IScore {

        private final String playerName;
        private final int points;

        public Score(String playerName, int points) {
            this.playerName = playerName;
            this.points = points;
        }

        @Override
        public String getPlayerName() {
            return playerName;
        }

        @Override
        public int getPoints() {
            return this.points;
        }

    }

}

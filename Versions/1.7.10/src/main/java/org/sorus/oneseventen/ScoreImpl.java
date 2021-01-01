

package org.sorus.oneseventen;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.sorus.client.version.game.IScore;

public class ScoreImpl implements IScore {

    private final Score score;

    public ScoreImpl(Score score) {
        this.score = score;
    }

    @Override
    public String getPlayerName() {
        ScorePlayerTeam scorePlayerTeam = score.getScoreScoreboard().getPlayersTeam(score.getPlayerName());
        return ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName());
    }

    @Override
    public int getPoints() {
        return score.getScorePoints();
    }

}

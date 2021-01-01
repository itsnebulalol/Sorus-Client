

package org.sorus.onesixteenfour;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import org.sorus.client.version.game.IScore;

public class ScoreImpl implements IScore {

    private final Score score;

    public ScoreImpl(Score score) {
        this.score = score;
    }

    @Override
    public String getPlayerName() {
        ScorePlayerTeam scorePlayerTeam = score.getScoreScoreboard().getPlayersTeam(score.getPlayerName());
        return ScorePlayerTeam.func_237500_a_(scorePlayerTeam, ITextComponent.func_244388_a(score.getPlayerName())).getString();
    }

    @Override
    public int getPoints() {
        return score.getScorePoints();
    }

}

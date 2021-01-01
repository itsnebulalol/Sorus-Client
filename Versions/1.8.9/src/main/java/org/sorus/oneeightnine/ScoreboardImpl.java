

package org.sorus.oneeightnine;

import net.minecraft.scoreboard.Scoreboard;
import org.sorus.client.version.game.IScoreObjective;
import org.sorus.client.version.game.IScoreboard;

public class ScoreboardImpl implements IScoreboard {

    private final Scoreboard scoreboard;

    public ScoreboardImpl(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public IScoreObjective getObjectiveInSlot(int slot) {
        return new ScoreObjectiveImpl(this.scoreboard.getObjectiveInDisplaySlot(slot));
    }

    @Override
    public IScoreObjective getDummyObjective() {
        return new DummyScoreboardObjective();
    }

}

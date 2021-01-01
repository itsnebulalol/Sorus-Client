

package org.sorus.oneseventen;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import org.sorus.client.version.game.IScore;
import org.sorus.client.version.game.IScoreObjective;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreObjectiveImpl implements IScoreObjective {

    private final ScoreObjective objective;

    public ScoreObjectiveImpl(ScoreObjective objective) {
        this.objective = objective;
    }

    @Override
    public boolean exists() {
        return objective != null;
    }

    @Override
    public String getName() {
        return objective.getDisplayName();
    }

    @Override
    public List<IScore> getScores() {
        Collection<Score> collection = objective.getScoreboard().getSortedScores(objective);
        List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());
        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }
        List<IScore> scores = new ArrayList<>();
        for(Score score : collection) {
            scores.add(new ScoreImpl(score));
        }
        Collections.reverse(scores);
        return scores;
    }

}

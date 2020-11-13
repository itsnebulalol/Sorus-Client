/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

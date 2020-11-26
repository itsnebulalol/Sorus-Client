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

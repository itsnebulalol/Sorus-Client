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

package org.sorus.client.module.impl.music;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;

public class Playlist {

  private final List<ISound> players = new ArrayList<>();

  private int currentPosition = 0;
  private double currentPercent;
  private ISound currentSound;

  private final Music music;

  private boolean playing;

  public Playlist() {
    this.music = Sorus.getSorus().getModuleManager().getModule(Music.class);
  }

  public void add(ISound player) {
    this.players.add(player);
  }

  public void resume() {
    this.resume(currentPosition, currentPercent);
  }

  public void onComplete() {
    int currentPosition = this.currentPosition + 1;
    if (currentPosition >= players.size()) {
      currentPosition = 0;
    }
    this.resume(currentPosition, 0);
  }

  private void resume(int position, double percent) {
    this.currentPosition = position;
    this.currentPercent = percent;
    ISound sound = this.players.get(currentPosition);
    sound.addCompletionHook(this::onComplete);
    sound.play(percent);
    this.music.setCurrent(this, sound);
    playing = true;
    this.currentSound = sound;
  }

  public void stop() {
    ISound sound = this.players.get(currentPosition);
    this.currentPercent = sound.getPlayPercent();
    sound.stop();
    playing = false;
  }

  public void skip(double percent) {
    if (this.playing) {
      this.stop();
      this.currentPercent = percent;
      this.resume();
    } else {
      this.currentPercent = percent;
    }
  }

  public void next() {
    int currentPosition = this.currentPosition + 1 >= players.size() ? 0 : this.currentPosition + 1;
    double currentPercent = 0;
    if(this.playing) {
      this.stop();
      this.resume(currentPosition, currentPercent);
    } else {
      this.currentPosition = currentPosition;
      this.currentPercent = currentPercent;
    }
  }

  public void back() {
    int currentPosition = this.currentPosition - 1 < 0 ? players.size() - 1 : this.currentPosition - 1;
    double currentPercent = 0;
    if(this.playing) {
      this.stop();
      this.resume(currentPosition, currentPercent);
    } else {
      this.currentPosition = currentPosition;
      this.currentPercent = currentPercent;
    }
  }

  public double getPlayPercent() {
    return this.currentSound.playing() ? this.currentSound.getPlayPercent() : this.currentPercent;
  }
}

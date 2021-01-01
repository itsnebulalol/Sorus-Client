package org.sorus.client.module.impl.music;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;

public class Playlist {

  private final List<ISound> sounds = new ArrayList<>();

  private int currentPosition = 0;
  private double currentPercent;
  private ISound currentSound;

  private final Music music;

  private boolean playing;

  public Playlist() {
    this.music = Sorus.getSorus().getModuleManager().getModule(Music.class);
  }

  public void add(ISound player) {
    this.sounds.add(player);
  }

  public void resume() {
    this.resume(currentPosition, currentPercent);
  }

  public void onComplete() {
    int currentPosition = this.currentPosition + 1;
    if (currentPosition >= sounds.size()) {
      currentPosition = 0;
    }
    this.resume(currentPosition, 0);
  }

  private void resume(int position, double percent) {
    this.currentPosition = position;
    this.currentPercent = percent;
    ISound sound = this.sounds.get(currentPosition);
    sound.addCompletionHook(this::onComplete);
    sound.play(percent);
    this.music.setCurrent(this, sound);
    playing = true;
    this.currentSound = sound;
  }

  public void stop() {
    ISound sound = this.sounds.get(currentPosition);
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
    int currentPosition = this.currentPosition + 1 >= sounds.size() ? 0 : this.currentPosition + 1;
    double currentPercent = 0;
    if (this.playing) {
      this.stop();
      this.resume(currentPosition, currentPercent);
    } else {
      this.currentPosition = currentPosition;
      this.currentPercent = currentPercent;
    }
  }

  public void back() {
    int currentPosition =
        this.currentPosition - 1 < 0 ? sounds.size() - 1 : this.currentPosition - 1;
    double currentPercent = 0;
    if (this.playing) {
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

  public List<ISound> getSounds() {
    return sounds;
  }

  public int getSongCount() {
    return this.sounds.size();
  }

  public boolean isPlaying() {
    return playing;
  }
}

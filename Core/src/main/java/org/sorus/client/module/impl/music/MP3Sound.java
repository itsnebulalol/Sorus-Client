package org.sorus.client.module.impl.music;

import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;

public class MP3Sound extends PlaybackListener implements ISound {

  private final String name;
  private final LambdaGetter<InputStream> inputStream;

  private InternalPlayer player;
  private ICompletionHook hook;
  private double percent;
  private boolean playing;
  private double savedPercent;

  public MP3Sound(String name, LambdaGetter<InputStream> inputStream) {
    this.name = name;
    this.inputStream = inputStream;
  }

  @Override
  public void play(double percent) {
    this.playing = true;
    this.percent = percent;
    try {
      this.player = new InternalPlayer();
      this.player.play(this.percent);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    this.playing = false;
    this.player.stop();
    this.player.thread.interrupt();
  }

  @Override
  public boolean playing() {
    return playing;
  }

  @Override
  public double getPlayPercent() {
    if (!playing) {
      return this.savedPercent;
    }
    try {
      return (this.player.length - this.player.inputStream.available())
          / (double) this.player.length;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public void addCompletionHook(ICompletionHook hook) {
    this.hook = hook;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isComplete() {
    return false;
  }

  private class InternalPlayer {

    private final int length;
    private AdvancedPlayer player;
    private final InputStream inputStream;
    private boolean playing = false;
    private Thread thread;

    public InternalPlayer() throws Throwable {
      this.inputStream = MP3Sound.this.inputStream.get();
      this.length = inputStream.available();
    }

    public void play(double percent) {
      thread =
          new Thread(
              () -> {
                try {
                  inputStream.skip((int) (percent * length));
                  player = new AdvancedPlayer(inputStream);
                  playing = true;
                  player.play();
                  if (playing) {
                    hook.onComplete();
                  }
                } catch (JavaLayerException | IOException e) {
                  e.printStackTrace();
                }
              });
      thread.start();
    }

    public void stop() {
      new Thread(
              () -> {
                while (!playing) {}
                savedPercent = MP3Sound.this.getPlayPercent();
                playing = false;
                player.close();
              })
          .start();
    }
  }
}

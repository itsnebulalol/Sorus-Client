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

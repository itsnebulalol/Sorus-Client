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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URI;

public class M4ASound implements ISound {

  private final String name;

  private InternalPlayer player;
  private ICompletionHook hook;
  private double percent;
  private boolean playing;
  private double savedPercent;
  private final URI uri;

  public M4ASound(String name, URI uri) {
    this.name = name;
    this.uri = uri;
  }

  @Override
  public void play(double percent) {
    this.playing = true;
    this.percent = percent;
    try {
      this.player = new InternalPlayer(uri);
      this.player.play(this.percent);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    this.playing = false;
    this.player.stop();
    //this.player.thread.interrupt();
  }

  @Override
  public boolean playing() {
    return playing;
  }

  @Override
  public double getPlayPercent() {
    /*if (!playing) {
      return this.savedPercent;
    }
    try {
      return (this.player.length - this.player.inputStream.available())
          / (double) this.player.length;
    } catch (Exception e) {*/
      return 0;
    //}
  }

  @Override
  public void addCompletionHook(ICompletionHook hook) {
    this.hook = hook;
  }

  @Override
  public String getName() {
    return name;
  }

  private class InternalPlayer {

    private MediaPlayer player;
    private boolean playing = false;
    private Thread thread;

    public InternalPlayer(URI uri) {
      Media media = new Media(uri.toString());
      this.player = new MediaPlayer(media);
    }

    public void play(double percent) {
      this.player.pause();
      this.player.seek(this.player.getTotalDuration().multiply(percent));
      this.player.play();
    }

    public void stop() {
      this.player.stop();
    }
  }
}

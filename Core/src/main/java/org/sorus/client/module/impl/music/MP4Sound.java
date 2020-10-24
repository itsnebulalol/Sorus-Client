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

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javazoom.jl.player.advanced.PlaybackListener;
import net.sourceforge.jaad.spi.javasound.AACAudioFileReader;

public class MP4Sound extends PlaybackListener implements ISound {

  private final String name;

  private ICompletionHook hook;
  private boolean playing;
  private Clip clip;
  private Thread thread;

  public MP4Sound(String name, File file) {
    this.name = name;
    new Thread(
            () -> {
              try {
                AACAudioFileReader aacAudioFileReader = new AACAudioFileReader();
                AudioInputStream stream = aacAudioFileReader.getAudioInputStream(file);
                this.clip = AudioSystem.getClip();
                clip.open(stream);
              } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  @Override
  public void play(double percent) {
    thread =
        new Thread(
            () -> {
              try {
                clip.setMicrosecondPosition((long) (clip.getMicrosecondLength() * percent));
                clip.start();
                this.playing = true;
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
    thread.start();
  }

  @Override
  public void stop() {
    this.playing = false;
    this.clip.stop();
    this.thread.interrupt();
  }

  @Override
  public boolean playing() {
    return playing;
  }

  @Override
  public double getPlayPercent() {
    try {
      return (double) this.clip.getMicrosecondPosition() / this.clip.getMicrosecondLength();
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
    return playing && !this.clip.isRunning();
  }
}

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

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.cshubhamrao.MediaConverter.Library.FFMpegLoader;
import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.quality.AudioQuality;
import com.github.kiulian.downloader.parser.DefaultParser;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.impl.music.screen.MainMusicScreen;
import org.sorus.client.version.input.Key;

public class Music extends ModuleConfigurable {

  private final List<Playlist> playlists = new ArrayList<>();

  private Playlist playlist;
  private ISound current;

  private boolean playing;

  public Music() {
    super("MUSIC");
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onStart(StartEvent e) {
    Playlist playlist = new Playlist();
    playlist.add(SoundConverter.fromFile(new File("songs/test.m4a")));
    playlist.add(SoundConverter.fromFile(new File("Boat Horn.mp3")));
    playlist.add(SoundConverter.fromFile(new File("Actual Song.mp3")));
    this.playlists.add(playlist);
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (e.getKey() == Key.M && Sorus.getSorus().getVersion().getGame().isIngame()) {
      Sorus.getSorus().getGUIManager().open(new MainMusicScreen());
    }
  }

  public List<Playlist> getPlaylists() {
    return playlists;
  }

  public void setCurrent(Playlist playlist, ISound current) {
    this.playlist = playlist;
    this.current = current;
  }

  public Playlist getCurrentPlaylist() {
    return playlist;
  }

  public ISound getCurrentSound() {
    return current;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
  }

  public boolean isPlaying() {
    return playing;
  }
}

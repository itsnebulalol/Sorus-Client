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

import java.awt.*;
import java.util.*;
import java.util.List;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.StartEvent;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.module.impl.music.screen.MainMusicScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.Key;

public class Music extends ModuleConfigurable {

  private final List<Playlist> playlists = new ArrayList<>();

  private Playlist playlist;
  private ISound current;

  private boolean playing;

  public Music() {
    super("Music");
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onStart(StartEvent e) {
    Playlist playlist = new Playlist();
    this.playlists.add(playlist);
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    if (this.getCurrentSound() != null) {
      if (this.getCurrentSound().getPlayPercent() > 0.99) {
        this.getCurrentPlaylist().next();
      }
    }
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (e.getKey() == Key.M && Sorus.getSorus().getVersion().getData(IGame.class).isIngame()) {
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

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Play your favourite music in minecraft!";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/music/logo.png").size(80, 80).color(new Color(175, 175, 175)));
  }

}

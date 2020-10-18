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

package org.sorus.client.module.impl.music.screen;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.cshubhamrao.MediaConverter.Library.FFMpegLoader;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.Format;
import com.github.kiulian.downloader.model.quality.AudioQuality;
import com.github.kiulian.downloader.parser.DefaultParser;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.util.ColorUtil;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.impl.chatmacros.MacrosSetting;
import org.sorus.client.module.impl.music.ISound;
import org.sorus.client.module.impl.music.Music;
import org.sorus.client.module.impl.music.Playlist;
import org.sorus.client.version.input.Key;

public class MainMusicScreen extends Screen {

  private final Panel main;
  private final Scroll scroll;
  private final Text text;
  private final MusicSlider slider;

  public MainMusicScreen() {
    main = new Panel();
    Collection menu = new Collection();
    menu.position(1520, 0);
    main.add(menu);
    menu.add(
        new Rectangle()
            .smooth(5)
            .size(405, 1080)
            .position(0, 0)
            .color(DefaultTheme.getBackgroundLayerColor()));
    menu.add(
        new Rectangle().size(405, 85).position(0, 5).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(180, 270)
            .position(0, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(90, 180)
            .position(390, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle().size(390, 5).position(5, 0).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(400, 7)
            .position(0, 90));
    menu.add(new AddMusicBox().position(180, 870));
    menu.add(
        new Rectangle()
            .size(380, 130)
            .position(10, 935)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(380, 4)
            .position(10, 931));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(390, 931));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, 130)
            .position(390, 935));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, 4)
            .position(390, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(380, 4)
            .position(10, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(6, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 100)
            .position(6, 965));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(6, 961));
    menu.add(
        text =
            new Text()
                .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                .scale(2.5, 2.5)
                .color(DefaultTheme.getForegroundLessLayerColor()));
    menu.add(slider = new MusicSlider().position(80, 980));
    menu.add(new PlayPauseComponent().position(180, 1010));
    menu.add(new NavigateComponent(NavigateComponent.Type.BACKWARD).position(120, 1010));
    menu.add(new NavigateComponent(NavigateComponent.Type.FORWARD).position(240, 1010));
    this.scroll = new Scroll().position(5, 95);
    menu.add(scroll);
    this.updatePlaylists();
    Sorus.getSorus().getEventManager().register(this);
  }

  private void updatePlaylists() {
    scroll.clear();
    int i = 0;
    for (Playlist playlist :
        Sorus.getSorus().getModuleManager().getModule(Music.class).getPlaylists()) {
      scroll.add(new PlaylistComponent(playlist).position(0, i * 115));
      i++;
    }
  }

  @Override
  public void onRender() {
    Music music = Sorus.getSorus().getModuleManager().getModule(Music.class);
    Playlist playlist = music.getCurrentPlaylist();
    ISound sound = music.getCurrentSound();
    text.text(sound != null ? sound.getName() : "No Song Playing")
        .position(200 - text.width() / 2 * 2.5, 955);
    if (slider.isEditing()) {
      if (sound != null) {
        Sorus.getSorus()
            .getModuleManager()
            .getModule(Music.class)
            .getCurrentPlaylist()
            .skip(slider.getValue());
      }
    } else {
      slider.setValue(sound != null ? playlist.getPlayPercent() : 0);
    }
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void onExit() {
    this.main.onRemove();
    super.onExit();
  }

  @Override
  public void keyTyped(Key key) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this);
    }
  }

  @Override
  public boolean shouldTakeOutOfGame() {
    return true;
  }

  public static class PlayPauseComponent extends Collection {

    private final Collection main;
    private final Music music;

    private double hoverPercent;

    private long prevRenderTime;

    public PlayPauseComponent() {
      this.music = Sorus.getSorus().getModuleManager().getModule(Music.class);
      Sorus.getSorus().getEventManager().register(this);
      this.main = new Collection();
      this.add(main);
    }

    @Override
    public void onRender() {
      main.clear();
      if (music.getCurrentSound() != null && music.getCurrentSound().playing()) {
        main.add(new Image().resource("sorus/modules/music/pause_button.png").size(40, 40));
      } else {
        main.add(new Image().resource("sorus/modules/music/play_button.png").size(40, 40));
      }
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          this.isHovered(
                  Sorus.getSorus().getVersion().getInput().getMouseX(),
                  Sorus.getSorus().getVersion().getInput().getMouseY())
              && music.getCurrentSound() != null;
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main.color(
          new Color(
              (int) (145 + 55 * hoverPercent),
              (int) (145 + 55 * hoverPercent),
              (int) (145 + 55 * hoverPercent)));
      prevRenderTime = renderTime;
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (music.getCurrentSound() != null) {
        if (this.isHovered(e.getX(), e.getY())) {
          if (music.getCurrentSound().playing()) {
            music.getCurrentPlaylist().stop();
            music.setPlaying(false);
          } else {
            music.getCurrentPlaylist().resume();
            music.setPlaying(true);
          }
        }
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public static class NavigateComponent extends Collection {

    private final Type type;
    private final Collection main;
    private final Music music;

    private double hoverPercent;

    private long prevRenderTime;

    public NavigateComponent(Type type) {
      this.type = type;
      this.music = Sorus.getSorus().getModuleManager().getModule(Music.class);
      Sorus.getSorus().getEventManager().register(this);
      this.main = new Collection();
      this.add(main);
      switch (type) {
        case FORWARD:
          main.add(new Image().resource("sorus/modules/music/forward_button.png").size(40, 40));
          break;
        case BACKWARD:
          main.add(new Image().resource("sorus/modules/music/backward_button.png").size(40, 40));
          break;
      }
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          this.isHovered(
                  Sorus.getSorus().getVersion().getInput().getMouseX(),
                  Sorus.getSorus().getVersion().getInput().getMouseY())
              && music.getCurrentSound() != null;
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main.color(
          new Color(
              (int) (145 + 55 * hoverPercent),
              (int) (145 + 55 * hoverPercent),
              (int) (145 + 55 * hoverPercent)));
      prevRenderTime = renderTime;
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (music.getCurrentSound() != null) {
        if (this.isHovered(e.getX(), e.getY())) {
          switch(type) {
            case FORWARD:
              music.getCurrentPlaylist().next();
              break;
            case BACKWARD:
              music.getCurrentPlaylist().back();
              break;
          }
        }
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }

    public enum Type {
      FORWARD,
      BACKWARD
    }
  }

  public static class AddMusicBox extends Collection {

    private final Collection main;
    private boolean selected;
    private boolean prevSelected = true;

    public AddMusicBox() {
      this.main = new Collection();
      this.add(main);
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if(prevSelected != selected) {
        main.clear();
        if(selected) {
          TextBoxInner textBox;
          main.add(textBox = new TextBoxInner("").position(-100, 0));
          textBox.selected = true;
        } else {
          main.add(new HollowRectangle().size(40, 40));
        }
        prevSelected = selected;
      }
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected = this.isHovered(e.getX(), e.getY());
    }

    private boolean isHovered(double x, double y) {
      if(selected) {
        return x > this.absoluteX() - 100 * this.absoluteXScale() && x < this.absoluteX() + 100 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 40 * this.absoluteYScale();
      } else {
        return x > this.absoluteX() && x < this.absoluteX() + 40 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 40 * this.absoluteYScale();
      }
    }

    public class TextBoxInner extends Collection {

      private final Rectangle rectangle;
      private final HollowRectangle hollowRectangle;
      private final Text text;

      private boolean selected;

      private String message;

      public TextBoxInner(String string) {
        this.add(rectangle = new Rectangle().smooth(5).size(250, 50));
        this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5).size(250, 50));
        this.add(
                text =
                        new Text()
                                .fontRenderer(
                                        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                                .scale(4, 4));
        this.message = string;
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRender() {
        if (selected) {
          rectangle.color(new Color(15, 15, 15, 175));
          hollowRectangle.color(new Color(235, 235, 235, 255));
          text.color(new Color(235, 235, 235, 255));
        } else {
          rectangle.color(new Color(15, 15, 15, 125));
          hollowRectangle.color(new Color(235, 235, 235, 210));
          text.color(new Color(235, 235, 235, 210));
        }
        this.updateButton(message);
        this.rectangle.onRender();
        this.hollowRectangle.onRender();
        Sorus.getSorus()
                .getVersion()
                .getGLHelper()
                .beginScissor(
                        this.absoluteX() + 10 * this.absoluteXScale(),
                        this.absoluteY(),
                        240 * this.absoluteXScale(),
                        50 * this.absoluteYScale());
        this.text.onRender();
        Sorus.getSorus().getVersion().getGLHelper().endScissor();
      }

      @Override
      public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
      }

      @EventInvoked
      public void onClick(MousePressEvent e) {
        selected =
                e.getX() > this.absoluteX()
                        && e.getX() < this.absoluteX() + 250 * this.absoluteXScale()
                        && e.getY() > this.absoluteY()
                        && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
      }

      @EventInvoked
      public void keyPressed(KeyPressEvent e) {
        if (selected) {
          char character = e.getCharacter();
          switch (e.getKey()) {
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
              return;
            case BACKSPACE:
              if (!message.isEmpty()) {
                this.message = message.substring(0, message.length() - 1);
              }
              break;
            case ENTER:
              this.downloadFromYT(message);
              break;
            default:
              this.message = message + character;
              break;
          }
        }
      }

      private void downloadFromYT(String url) {
        new Thread(() -> {
          try {
            String videoID = null;
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
            if (matcher.find()) {
              videoID =  matcher.group();
            }
            YoutubeDownloader downloader = new YoutubeDownloader(new DefaultParser());
            downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
            downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            downloader.setParserRetryOnFailure(1);
            YoutubeVideo video = downloader.getVideo(videoID);
            List<AudioFormat> audioFormats = video.findAudioWithQuality(AudioQuality.low);
            File outputDir = new File("songs");
            Format format = audioFormats.get(0);
            File download = video.download(format, outputDir);
            new FFMpegLoader().run();
            ProcessBuilder processBuilder = new ProcessBuilder(FFMpegLoader.getFFMpegExecutable().getAbsolutePath(), "-i", download.getAbsolutePath(), new File(download.getAbsolutePath().replace(".weba", ".mp3")).getAbsolutePath());
            Process process = processBuilder.start();
            process.waitFor();
            download.delete();
          } catch(Exception e) {
            e.printStackTrace();
          }
        }).start();
      }

      private void updateButton(String string) {
        this.text.text(string + " ");
        if (this.text.width() > 220 * this.absoluteXScale()) {
          this.text.position(
                  10 + 220 - (this.text.width() * 1 / this.absoluteXScale()),
                  25 - this.text.height() / 2 * 4);
        } else {
          this.text.position(10, 25 - this.text.height() / 2 * 4);
        }
        if (selected) {
          this.text.text(string + (System.currentTimeMillis() % 1000 > 500 ? "_" : ""));
        }
      }
    }

  }

}

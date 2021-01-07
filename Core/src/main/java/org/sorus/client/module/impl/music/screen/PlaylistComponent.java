package org.sorus.client.module.impl.music.screen;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.module.impl.music.Playlist;
import org.sorus.client.version.input.IInput;

public class PlaylistComponent extends Collection {

  private final MainMusicScreen mainMusicScreen;
  private final Playlist playlist;

  public PlaylistComponent(MainMusicScreen mainMusicScreen, Playlist playlist) {
    this.mainMusicScreen = mainMusicScreen;
    this.playlist = playlist;
    /*this.add(
        new Rectangle().size(380, 70).position(4, 4).color(defaultTheme.getMedgroundLayerColor()));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(380, 4)
            .position(4, 0));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(384, 0));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor())
            .size(4, 70)
            .position(384, 4));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor())
            .size(4, 4)
            .position(384, 74));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor())
            .size(380, 4)
            .position(4, 74));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(0, 74));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 70)
            .position(0, 4));
    this.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(0, 0));*/
    this.add(new SettingsButton().position(280, 17.5));
    this.add(new PlayButton().position(330, 17.5));
  }

  @Override
  public void onRender() {
    super.onRender();
  }

  public class PlayButton extends Collection {

    private final Collection main;

    private double hoverPercent;

    private long prevRenderTime;

    public PlayButton() {
      this.add(main = new Collection());
      main.add(new Image().resource("sorus/modules/music/play_button.png").size(40, 40));
      // main.add(new Rectangle().size(40, 40).smooth(10).color(new Color(25, 160, 65)));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          this.isHovered(
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main
          .position(-hoverPercent * 0.5, -hoverPercent * 1.125)
          .scale(1 + hoverPercent * 0.05, 1 + hoverPercent * 0.05)
          .color(
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
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        playlist.resume();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class SettingsButton extends Collection {

    private final Collection main;

    private double hoverPercent;

    private long prevRenderTime;

    public SettingsButton() {
      this.add(main = new Collection());
      main.add(new Image().resource("sorus/gear.png").size(40, 40));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          this.isHovered(
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      this.main
          .position(-hoverPercent * 0.5, -hoverPercent * 1.125)
          .scale(1 + hoverPercent * 0.05, 1 + hoverPercent * 0.05)
          .color(
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
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus().getGUIManager().close(PlaylistComponent.this.mainMusicScreen);
        Sorus.getSorus().getGUIManager().open(new PlaylistEditScreen(playlist));
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }
}

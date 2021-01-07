package org.sorus.client.module.impl.music.screen;

import java.io.File;
import java.util.Objects;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Scroll;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.module.impl.music.ISound;
import org.sorus.client.module.impl.music.SoundConverter;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class SelectFromLibraryScreen extends Screen {

  private final Callback<ISound> callback;

  private Panel main;
  private Scroll scroll;

  public SelectFromLibraryScreen(Callback<ISound> callback) {
    this.callback = callback;
  }

  @Override
  public void onOpen() {
    this.main = new Panel();
    Collection menu = new Collection();
    menu.position(1520, 0);
    main.add(menu);
    /*menu.add(
        new Rectangle()
            .smooth(5)
            .size(405, 1080)
            .position(0, 0)
            .color(defaultTheme.getBackgroundLayerColor()));
    menu.add(
        new Rectangle().size(405, 65).position(0, 5).color(defaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(180, 270)
            .position(0, 0)
            .color(defaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(90, 180)
            .position(390, 0)
            .color(defaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle().size(390, 5).position(5, 0).color(defaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor())
            .size(400, 7)
            .position(0, 70));
    menu.add(
        new Rectangle()
            .size(380, 130)
            .position(10, 935)
            .color(defaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(380, 4)
            .position(10, 931));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(390, 931));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor())
            .size(4, 130)
            .position(390, 935));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor())
            .size(4, 4)
            .position(390, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor())
            .size(380, 4)
            .position(10, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(6, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 100)
            .position(6, 965));
    menu.add(
        new Rectangle()
            .gradient(
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowStartColor(),
                defaultTheme.getShadowEndColor(),
                defaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(6, 961));
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    menu.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text("SORUS")
            .position(200 - fontRenderer.getStringWidth("SORUS") / 2 * 5.5, 15)
            .scale(5.5, 5.5)
            .color(defaultTheme.getForegroundLayerColor()));
    menu.add(
        new ExitButton(
                () -> {
                  Sorus.getSorus().getGUIManager().close(this);
                  callback.cancel();
                })
            .position(10, 10));*/
    menu.add(scroll = new Scroll().position(10, 80));
    int i = 0;
    File music = new File("sorus/music");
    music.mkdirs();
    for (File file : Objects.requireNonNull(music.listFiles())) {
      scroll.add(
          new SongComponent(file.getName().substring(0, file.getName().lastIndexOf('.')))
              .position(0, i * 85));
      i++;
    }
  }

  @Override
  public void onRender() {
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void onExit() {
    main.onRemove();
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this);
    }
  }

  private void select(SongComponent songComponent) {
    File music = new File("sorus/music");
    music.mkdirs();
    for (File file : Objects.requireNonNull(music.listFiles())) {
      if (file.getName().contains(songComponent.name)) {
        callback.call(SoundConverter.fromMP4File(file));
      }
    }
  }

  @Override
  public boolean shouldOpenBlank() {
    return true;
  }

  public class SongComponent extends Collection {

    private final String name;

    public SongComponent(String name) {
      this.name = name;
      /*this.add(new Rectangle().size(380, 75).color(defaultTheme.getMedgroundLayerColor()));
      this.add(new Select().position(10, 45));
      this.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text(name)
              .scale(2, 2)
              .position(10, 13)
              .color(defaultTheme.getForegroundLessLayerColor()));*/
    }

    public class Select extends Collection {

      private double hoveredPercent;

      public Select() {
        this.add(new Rectangle().size(5, 20).position(7.5, 0));
        this.add(new Rectangle().size(20, 5).position(0, 7.5));
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRender() {
        boolean hovered =
            this.isHovered(
                Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
                Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
        this.hoveredPercent = MathUtil.clamp(hoveredPercent + (hovered ? 1 : -1) * 0.05, 0, 1);
        /*this.color(
        ColorUtil.getBetween(
            defaultTheme.getForegroundLessLayerColor(),
            defaultTheme.getForegroundLayerColor(),
            hoveredPercent));*/
        super.onRender();
      }

      @Override
      public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
      }

      @EventInvoked
      public void onClick(MousePressEvent e) {
        if (this.isHovered(e.getX(), e.getY())) {
          Sorus.getSorus().getGUIManager().close(SelectFromLibraryScreen.this);
          SelectFromLibraryScreen.this.select(SongComponent.this);
        }
      }

      private boolean isHovered(double x, double y) {
        return x > this.absoluteX()
            && x < this.absoluteX() + 20 * this.absoluteXScale()
            && y > this.absoluteY()
            && y < this.absoluteY() + 20 * this.absoluteYScale();
      }
    }
  }
}

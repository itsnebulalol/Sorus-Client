package org.sorus.client.gui.theme.defaultTheme.profilelist;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.gui.screen.profilelist.ProfileListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.defaultTheme.DefaultSomethingScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.modulelist.ModuleListComponent;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultProfileListScreen extends DefaultSomethingScreen<ProfileListScreen> {

  private static final double SEPARATION = 15;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  private int profileCount;

  public DefaultProfileListScreen(DefaultTheme theme) {
    super(theme, false, 5);
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    this.main = new Panel();
    Collection menu = new Collection().position(510, 240);
    main.add(menu);
    this.addGuiPreComponents(menu);
    menu.add(new Scissor().size(870, 440).add(scroll = new Scroll()).position(0, 80));
    this.updateProfiles();
    menu.add(scrollBar = new ScrollBar().position(870, 90));
    this.addGuiPostComponents(menu);
    Text title =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text("PROFILES")
            .scale(6, 6)
            .color(defaultTheme.getElementColorNew());
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    menu.add(new ExitButton(this, this::close).position(10, 15));
    Collection collection = new Collection().position(830, 12.5);
    collection.add(
        new Rectangle().size(55, 55).smooth(10).color(defaultTheme.getBackgroundColorNew()));
    collection.add(new Add().position(7.5, 7.5));
    menu.add(collection);
    super.init();
  }

  private void addGuiPreComponents(Collection collection) {
    collection.add(
        new Rectangle().size(900, 600).smooth(10).color(defaultTheme.getBackgroundColorNew()));
  }

  private void addGuiPostComponents(Collection collection) {
    collection.add(
        new Rectangle()
            .size(900, 15)
            .gradient(
                defaultTheme.getGradientEndColorNew(),
                defaultTheme.getGradientEndColorNew(),
                defaultTheme.getGradientStartColorNew(),
                defaultTheme.getGradientStartColorNew())
            .position(0, 70));
    collection.add(
        new Rectangle()
            .size(900, 15)
            .gradient(
                defaultTheme.getGradientStartColorNew(),
                defaultTheme.getGradientStartColorNew(),
                defaultTheme.getGradientEndColorNew(),
                defaultTheme.getGradientEndColorNew())
            .position(0, 515));
    collection.add(
        new Rectangle().size(900, 80).smooth(10).color(defaultTheme.getForegroundColorNew()));
    collection.add(
        new Rectangle()
            .size(900, 80)
            .smooth(10)
            .position(0, 520)
            .color(defaultTheme.getForegroundColorNew()));
  }

  public void updateProfiles() {
    scroll.clear();
    profileCount = 0;
    File file = new File("sorus/settings");
    for (File file1 : Objects.requireNonNull(file.listFiles())) {
      scroll.add(
          new ProfileListComponent(this, file1.getName())
              .position(
                  SEPARATION,
                  SEPARATION + profileCount * (ProfileListComponent.HEIGHT + SEPARATION)));
      profileCount++;
    }
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
    super.render();
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll =
        MathUtil.clamp(
            targetScroll + scrollValue * 0.7, scroll.getMinScroll(), scroll.getMaxScroll());
    currentScroll = (targetScroll - currentScroll) * 12 / FPS + scroll.getScroll();
    scroll.setScroll(currentScroll);
    maxScroll = profileCount * (ModuleListComponent.HEIGHT + SEPARATION) - 420;
    scroll.addMinMaxScroll(-maxScroll, 0);
    double size = Math.min(1, 440 / (maxScroll + 440));
    this.scrollBar.updateScrollBar(-currentScroll / (maxScroll + 440), size);
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
  }

  private void setTargetScroll(double targetScroll) {
    this.targetScroll = targetScroll;
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
    super.exit();
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      this.close();
    }
  }

  public class Add extends Collection {

    private final Collection main;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(main = new Collection());
      main.add(
          new Rectangle()
              .size(40, 40)
              .smooth(5)
              .color(defaultTheme.getElementBackgroundColorNew()));
      main.add(
          new Rectangle().size(5, 30).position(17.5, 5).color(defaultTheme.getElementColorNew()));
      main.add(
          new Rectangle().size(30, 5).position(5, 17.5).color(defaultTheme.getElementColorNew()));
      main.attach(new Expand(100, 40, 40, 0.05));
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        File settings = new File("sorus/settings");
        File file = new File(settings, "newProfile0");
        int i = 0;
        while (file.exists()) {
          file = new File(settings, "newProfile" + i);
          i++;
        }
        Sorus.getSorus().getSettingsManager().load(file.getName());
        DefaultProfileListScreen.this.updateProfiles();
      }
    }

    private boolean isHovered(double mouseX, double mouseY) {
      double x = this.absoluteX();
      double y = this.absoluteY();
      double width = 40 * this.absoluteXScale();
      double height = 40 * this.absoluteYScale();
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }
  }

  public class ScrollBar extends Collection {

    private final Rectangle scrollBar;

    private double location, size;

    private boolean dragging;
    private double initialDragY;
    private double initialLocation;

    public ScrollBar() {
      this.add(
          new Rectangle()
              .size(18, 420)
              .smooth(9)
              .color(defaultTheme.getElementBackgroundColorNew()));
      this.add(
          scrollBar = new Rectangle().smooth(9).color(defaultTheme.getElementMedgroundColorNew()));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      this.scrollBar.size(18, 420 * size).position(0, 420 * location);
      if (dragging) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        double delta = mouseY - initialDragY;
        DefaultProfileListScreen.this.setTargetScroll(
            -(initialLocation + delta / (420 * scrollBar.absoluteYScale()))
                * (DefaultProfileListScreen.this.maxScroll + 440));
      }
      super.onRender();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (e.getX() > scrollBar.absoluteX()
          && e.getX() < scrollBar.absoluteX() + 18 * scrollBar.absoluteXScale()
          && e.getY() > scrollBar.absoluteY()
          && e.getY() < scrollBar.absoluteY() + 420 * size * scrollBar.absoluteYScale()) {
        dragging = true;
        initialDragY = e.getY();
        initialLocation = location;
      } else {
        dragging = false;
      }
    }

    @EventInvoked
    public void onRelease(MouseReleaseEvent e) {
      dragging = false;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    public void updateScrollBar(double location, double size) {
      this.location = location;
      this.size = size;
    }
  }
}

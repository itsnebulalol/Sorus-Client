package org.sorus.client.gui.theme.defaultTheme.theme;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.theme.SelectThemeScreen;
import org.sorus.client.gui.screen.theme.ThemeListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.Theme;
import org.sorus.client.gui.theme.ThemeManager;
import org.sorus.client.gui.theme.defaultTheme.DefaultSomethingScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.modulelist.ModuleListComponent;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultThemeListScreen extends DefaultSomethingScreen<ThemeListScreen> {

  private static final double SEPARATION = 15;

  private final ThemeManager themeManager;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;

  private int themeCount;

  private ThemeListComponent draggedComponent;
  private double initialMouseX, initialMouseY;
  private double initialX, initialY;

  public DefaultThemeListScreen(DefaultTheme theme) {
    super(theme, false, 4);
    this.themeManager = Sorus.getSorus().getThemeManager();
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    this.main = new Panel(this.getParent());
    Collection menu = new Collection().position(510, 240);
    main.add(menu);
    this.addGuiPreComponents(menu);
    menu.add(new Scissor().size(870, 440).add(scroll = new Scroll()).position(0, 80));
    menu.add(scrollBar = new ScrollBar().position(870, 90));
    this.addGuiPostComponents(menu);
    Text title =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text("THEMES")
            .scale(6, 6)
            .color(defaultTheme.getElementColorNew());
    menu.add(new ExitButton(this, this::close).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    this.updateThemes();
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

  public void updateThemes() {
    scroll.clear();
    themeCount = 0;
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    for (Theme theme : this.themeManager.getCurrentThemes()) {
      boolean added = false;
      while (!added) {
        boolean add = true;
        if (draggedComponent != null) {
          double y = MathUtil.clamp(draggedComponent.absoluteY(), 225 * yRatio, 1000 * yRatio);
          if (Math.abs(y - ((themeCount * 135) + 225) * yRatio) < 62.5 * yRatio) {
            add = false;
          }
        }
        if (add) {
          scroll.add(
              new ThemeListComponent(this, theme)
                  .position(
                      SEPARATION,
                      SEPARATION + themeCount * (ThemeListComponent.HEIGHT + SEPARATION)));
          added = true;
        }
        themeCount++;
      }
    }
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
    super.render();
    double xRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920;
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    if (draggedComponent != null) {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
      draggedComponent.position(
          (mouseX - initialMouseX + initialX) * 1 / xRatio,
          (mouseY - initialMouseY + initialY) * 1 / yRatio);
      this.updateThemes();
    }
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll =
        MathUtil.clamp(
            targetScroll + scrollValue * 0.7, scroll.getMinScroll(), scroll.getMaxScroll());
    currentScroll = (targetScroll - currentScroll) * 12 / FPS + scroll.getScroll();
    scroll.setScroll(currentScroll);
    double maxScroll =
        Math.ceil(themeCount / 2.0) * (ModuleListComponent.HEIGHT + SEPARATION) - 420;
    scroll.addMinMaxScroll(-maxScroll, 0);
    double size = Math.min(1, 440 / (maxScroll + 440));
    this.scrollBar.updateScrollBar(-currentScroll / (maxScroll + 440), size);
    main.scale(xRatio, yRatio);
    main.onRender();
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

  public void onComponentDrag(ThemeListComponent draggedComponent, double mouseX, double mouseY) {
    this.draggedComponent = new ThemeListComponent(this, draggedComponent.getTheme());
    this.initialMouseX = mouseX;
    this.initialMouseY = mouseY;
    this.initialX = draggedComponent.absoluteX();
    this.initialY = draggedComponent.absoluteY();
    themeManager.remove(draggedComponent.getTheme());
    main.add(this.draggedComponent);
    this.updateThemes();
  }

  public void onComponentRelease(ThemeListComponent component) {
    double yRatio = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080;
    int size = themeManager.getCurrentThemes().size();
    int index = 0;
    boolean added = false;
    while (!added) {
      if (draggedComponent != null) {
        double y =
            MathUtil.clamp(draggedComponent.absoluteY(), 225 * yRatio, (225 + size * 135) * yRatio);
        if (Math.abs(y - ((index * 135) + 225) * yRatio) < 62.5 * yRatio) {
          themeManager.getCurrentThemes().add(index, component.getTheme());
          added = true;
        }
      }
      index++;
    }
    main.remove(draggedComponent);
    draggedComponent = null;
    this.updateThemes();
  }

  public ThemeListComponent getDraggedComponent() {
    return draggedComponent;
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
        Sorus.getSorus().getGUIManager().close(DefaultThemeListScreen.this.getParent());
        Sorus.getSorus().getGUIManager().open(new SelectThemeScreen(new ThemeReceiver()));
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

    public class ThemeReceiver implements Callback<Theme> {

      @Override
      public void call(Theme selected) {
        themeManager.add(selected);
        Sorus.getSorus().getGUIManager().open(DefaultThemeListScreen.this.getParent());
        DefaultThemeListScreen.this.updateThemes();
      }

      @Override
      public void cancel() {
        Sorus.getSorus().getGUIManager().open(new ThemeListScreen());
      }
    }
  }

  public class ScrollBar extends Collection {

    private final Rectangle scrollBar;

    private double location, size;

    private boolean dragging;
    private double initialDragY;

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
        /*DefaultSettingsScreen.this.setTargetScroll(-(initialLocation + delta / (420 *
        scrollBar.absoluteYScale())) * (DefaultSettingsScreen.this.maxScroll + 440));*/
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

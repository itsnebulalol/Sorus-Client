package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.gui.screen.settings.SettingsHolder;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.screen.theme.ThemeListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.settings.SettingsManager;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultSettingsScreen extends DefaultSomethingScreen<SettingsScreen> {

  private final Screen parent;
  private final Screen returnScreen;
  private final IConfigurableScreen configurable;

  private Panel main;
  private Scroll scroll;
  private SettingsHolder settingsHolder;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;

  public DefaultSettingsScreen(
      DefaultTheme theme, Screen parent, Screen returnScreen, IConfigurableScreen configurable) {
    super(theme, false, DefaultSettingsScreen.getIndex(parent, configurable));
    this.parent = parent;
    this.returnScreen = returnScreen;
    this.configurable = configurable;
  }

  private static int getIndex(Screen parent, IConfigurableScreen configurable) {
    if (configurable instanceof SettingsManager) {
      return 3;
    }
    if (parent.getClass().equals(ModuleListScreen.class)) {
      return 1;
    }
    if (parent.getClass().equals(HUDListScreen.class)) {
      return 2;
    }
    if (parent.getClass().equals(ThemeListScreen.class)) {
      return 4;
    }
    return -1;
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
    settingsHolder = new SettingsHolder(this.configurable);
    scroll.add(settingsHolder);
    this.addGuiPostComponents(menu);
    Text title =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text("SETTINGS")
            .scale(6, 6)
            .color(defaultTheme.getElementColorNew());
    menu.add(new ExitButton(this, this::onExit).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
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
    double maxScroll = settingsHolder.getHeight() - 420;
    scroll.addMinMaxScroll(-maxScroll, 0);
    double size = Math.min(1, 440 / (maxScroll + 440));
    this.scrollBar.updateScrollBar(-currentScroll / (maxScroll + 440), size);
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    main.onRemove();
    super.exit();
  }

  private void onExit() {
    Sorus.getSorus().getGUIManager().close(this.getParent());
    Sorus.getSorus().getGUIManager().open(returnScreen);
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE && this.getParent().isTopLevel()) {
      Sorus.getSorus().getGUIManager().close(this.getParent());
    }
  }

  protected boolean shouldInitiateDrag(double mouseX, double mouseY) {
    IScreen screen = Sorus.getSorus().getVersion().getData(IScreen.class);
    double scaledMouseX = mouseX / screen.getScaledWidth() * 1920;
    double scaledMouseY = mouseY / screen.getScaledHeight() * 1080;
    return !(scaledMouseX > 510 && scaledMouseX < 1410 && scaledMouseY > 240 && scaledMouseY < 840);
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

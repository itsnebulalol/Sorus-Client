package org.sorus.client.gui.theme.defaultTheme.hudlist;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.SingleHUD;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
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

public class DefaultHUDListScreen extends DefaultSomethingScreen<HUDListScreen> {

  private static final double SEPARATION = 15;

  private final HUDManager hudManager;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  public DefaultHUDListScreen(DefaultTheme theme) {
    super(theme, false, 2);
    this.hudManager = Sorus.getSorus().getHUDManager();
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    this.main = new Panel();
    Collection menu = new Collection().position(510, 240);
    main.add(menu);
    this.addGuiPreComponents(menu);
    menu.add(new Scissor().size(870, 440).add(scroll = new Scroll()).position(0, 80));
    this.onSearchUpdate("");
    menu.add(scrollBar = new ScrollBar().position(870, 90));
    this.addGuiPostComponents(menu);
    Text title =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text("HUDS")
            .scale(6, 6)
            .color(defaultTheme.getElementColorNew());
    menu.add(new ExitButton(this, this::close).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    Collection collection = new Collection().position(767.5, 12.5);
    collection.add(
        new Rectangle().size(117.5, 55).smooth(10).color(defaultTheme.getBackgroundColorNew()));
    collection.add(new Add(AddType.SINGLE).position(7.5, 7.5));
    collection.add(new Add(AddType.MULTI).position(70, 7.5));
    menu.add(collection);
    // menu.add(new DefaultModuleListScreen2.Search().position(20, 535));
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
    int moduleCount = scroll.getChildren().size();
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll =
        MathUtil.clamp(
            targetScroll + scrollValue * 0.7, scroll.getMinScroll(), scroll.getMaxScroll());
    currentScroll = (targetScroll - currentScroll) * 12 / FPS + scroll.getScroll();
    scroll.setScroll(currentScroll);
    maxScroll = moduleCount * (ModuleListComponent.HEIGHT + SEPARATION) - 420;
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

  protected boolean shouldInitiateDrag(double mouseX, double mouseY) {
    IScreen screen = Sorus.getSorus().getVersion().getData(IScreen.class);
    double scaledMouseX = mouseX / screen.getScaledWidth() * 1920;
    double scaledMouseY = mouseY / screen.getScaledHeight() * 1080;
    return !(scaledMouseX > 510 && scaledMouseX < 1410 && scaledMouseY > 240 && scaledMouseY < 840);
  }

  public void onSearchUpdate(String searchTerm) {
    this.scroll.clear();
    int i = 0;
    for (HUD hud : hudManager.getHUDs()) {
      if (searchTerm.isEmpty() || hud.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
        this.scroll.add(
            new HUDListComponent(this, hud)
                .position(SEPARATION, SEPARATION + i * (HUDListComponent.HEIGHT + SEPARATION)));
        i++;
      }
    }
  }

  public class Add extends Collection {

    private final AddType type;

    public Add(AddType type) {
      this.type = type;
      Collection main = new Collection();
      if (type == AddType.SINGLE) {
        main.add(
            new Rectangle()
                .size(40, 40)
                .smooth(5)
                .color(defaultTheme.getElementBackgroundColorNew()));
      } else {
        main.add(
            new Rectangle()
                .size(32, 32)
                .smooth(5)
                .position(8, 8)
                .color(defaultTheme.getElementMedgroundColorNew()));
        main.add(
            new Rectangle()
                .size(32, 32)
                .smooth(5)
                .color(defaultTheme.getElementBackgroundColorNew()));
      }
      main.add(
          new Rectangle().size(5, 30).position(17.5, 5).color(defaultTheme.getElementColorNew()));
      main.add(
          new Rectangle().size(30, 5).position(5, 17.5).color(defaultTheme.getElementColorNew()));
      main.attach(new Expand(100, 40, 40, 0.05));
      this.add(main);
      Sorus.getSorus().getEventManager().register(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        HUD hud;
        if (this.type == AddType.SINGLE) {
          hud = new SingleHUD();
        } else {
          hud = new HUD();
        }
        Sorus.getSorus().getGUIManager().close(DefaultHUDListScreen.this.getParent());
        hud.onCreation();
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

  private enum AddType {
    SINGLE,
    MULTI
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
        DefaultHUDListScreen.this.setTargetScroll(
            -(initialLocation + delta / (420 * scrollBar.absoluteYScale()))
                * (DefaultHUDListScreen.this.maxScroll + 440));
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

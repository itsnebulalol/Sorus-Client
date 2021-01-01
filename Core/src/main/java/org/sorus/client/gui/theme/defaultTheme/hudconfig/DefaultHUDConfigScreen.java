package org.sorus.client.gui.theme.defaultTheme.hudconfig;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.gui.hud.*;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.defaultTheme.DefaultSomethingScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.hudlist.DefaultHUDListScreen;
import org.sorus.client.gui.theme.defaultTheme.modulelist.ModuleListComponent2;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultHUDConfigScreen extends DefaultSomethingScreen<HUDConfigScreen> {

  private static final double SEPARATION = 15;

  private final HUD hud;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  public DefaultHUDConfigScreen(HUD hud) {
    super(false, 2);
    this.hud = hud;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    this.main = new Panel();
    Collection menu = new Collection().position(510, 240);
    main.add(menu);
    this.addGuiPreComponents(menu);
    menu.add(new Scissor().size(870, 440).add(scroll = new Scroll()).position(0, 80));
    menu.add(scrollBar = new ScrollBar().position(870, 90));
    this.addGuiPostComponents(menu);
    Text title =
            new Text()
                    .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
                    .text("HUD")
                    .scale(6, 6)
                    .color(DefaultTheme.getElementColorNew());
    menu.add(new ExitButton(this::onExit).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    menu.add(new Add().position(420, 530));
    this.updateComponents();
    //collection.add(new DefaultHUDListScreen.Add(DefaultHUDListScreen.AddType.SINGLE).position(7.5, 7.5));
    //collection.add(new DefaultHUDListScreen.Add(DefaultHUDListScreen.AddType.MULTI).position(70, 7.5));
    super.init();
  }

  private void addGuiPreComponents(Collection collection) {
    collection.add(
            new Rectangle().size(900, 600).smooth(10).color(DefaultTheme.getBackgroundColorNew()));
  }

  private void addGuiPostComponents(Collection collection) {
    collection.add(
            new Rectangle()
                    .size(900, 15)
                    .gradient(
                            DefaultTheme.getGradientEndColorNew(),
                            DefaultTheme.getGradientEndColorNew(),
                            DefaultTheme.getGradientStartColorNew(),
                            DefaultTheme.getGradientStartColorNew())
                    .position(0, 70));
    collection.add(
            new Rectangle()
                    .size(900, 15)
                    .gradient(
                            DefaultTheme.getGradientStartColorNew(),
                            DefaultTheme.getGradientStartColorNew(),
                            DefaultTheme.getGradientEndColorNew(),
                            DefaultTheme.getGradientEndColorNew())
                    .position(0, 515));
    collection.add(
            new Rectangle().size(900, 80).smooth(10).color(DefaultTheme.getForegroundColorNew()));
    collection.add(
            new Rectangle()
                    .size(900, 80)
                    .smooth(10)
                    .position(0, 520)
                    .color(DefaultTheme.getForegroundColorNew()));
  }

  public void updateComponents() {
    scroll.clear();
    int i = 0;
    for (IComponent component : this.hud.getComponents()) {
      this.scroll.add(
              new ComponentComponent(this, (Component) component)
                      .position(
                              SEPARATION + ((i % 2) * (ModuleListComponent2.WIDTH + SEPARATION)),
                              SEPARATION + (int) (i / 2) * (ModuleListComponent2.HEIGHT + SEPARATION)));
      i++;
    }
  }

  @Override
  public void update() {
    main.onUpdate();
    super.update();
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
    maxScroll = Math.ceil(moduleCount / 2.0) * (ModuleListComponent2.HEIGHT + SEPARATION) - 420;
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
    Sorus.getSorus().getSettingsManager().save();
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    main.onRemove();
    super.exit();
  }

  private void onExit() {
    Sorus.getSorus().getGUIManager().close(this.getParent());
    Sorus.getSorus().getGUIManager().open(new HUDListScreen());
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE && this.getParent().isTopLevel()) {
      Sorus.getSorus().getGUIManager().close(this.getParent());
    }
  }

  public HUD getHUD() {
    return hud;
  }

  public class Add extends Collection {

    private final Collection main;

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      this.add(main = new Collection());
      main.add(new Rectangle().smooth(4).size(60, 60).color(DefaultTheme.getElementBackgroundColorNew()));
      Collection centerCross = new Collection();
      centerCross.add(new Rectangle().size(10, 40).position(25, 10));
      centerCross.add(new Rectangle().size(40, 10).position(10, 25));
      main.add(centerCross);
      main.attach(new Expand(100, 60, 60, 0.05));
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus().getGUIManager().close(DefaultHUDConfigScreen.this.getParent());
        Sorus.getSorus()
                .getGUIManager()
                .open(
                        new SelectComponentScreen(DefaultHUDConfigScreen.this.getParent(), new OnAddComponent()));
      }
    }

    private boolean isHovered(double mouseX, double mouseY) {
      double x = this.absoluteX();
      double y = this.absoluteY();
      double width = 60 * this.absoluteXScale();
      double height = 60 * this.absoluteYScale();
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    public class OnAddComponent implements Callback<IComponent> {

      @Override
      public void call(IComponent selected) {
        DefaultHUDConfigScreen.this.hud.addComponent(selected);
        DefaultHUDConfigScreen.this.hud.displaySettings(
                new HUDConfigScreen(DefaultHUDConfigScreen.this.hud));
      }

      @Override
      public void cancel() {}
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
                      .color(DefaultTheme.getElementBackgroundColorNew()));
      this.add(
              scrollBar = new Rectangle().smooth(9).color(DefaultTheme.getElementMedgroundColorNew()));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      this.scrollBar.size(18, 420 * size).position(0, 420 * location);
      if (dragging) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        double delta = mouseY - initialDragY;
        DefaultHUDConfigScreen.this.setTargetScroll(
                -(initialLocation + delta / (420 * scrollBar.absoluteYScale()))
                        * (DefaultHUDConfigScreen.this.maxScroll + 440));
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

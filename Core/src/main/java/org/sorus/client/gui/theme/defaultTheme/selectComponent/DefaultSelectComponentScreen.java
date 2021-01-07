package org.sorus.client.gui.theme.defaultTheme.selectComponent;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.IComponent;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.defaultTheme.DefaultSomethingScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultSelectComponentScreen extends DefaultSomethingScreen<SelectComponentScreen> {

  private static final double SEPARATION = 15;

  private final HUDManager hudManager;
  private final Screen parent;
  private final Callback<IComponent> callback;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  private int componentCount;

  private SelectComponent selected;

  public DefaultSelectComponentScreen(
      DefaultTheme theme, Screen parent, Callback<IComponent> callback) {
    super(theme, false, 2);
    this.parent = parent;
    this.callback = callback;
    this.hudManager = Sorus.getSorus().getHUDManager();
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
    componentCount = 0;
    for (Class<? extends Component> componentClass : this.hudManager.getRegisteredComponents()) {
      try {
        Component component = componentClass.newInstance();
        SelectComponent selectComponent = new SelectComponent(this, component);
        this.scroll.add(
            selectComponent.position(
                SEPARATION, SEPARATION + componentCount * (SelectComponent.HEIGHT + SEPARATION)));
        componentCount++;
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    this.addGuiPostComponents(menu);
    Text title =
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer())
            .text("HUDS")
            .scale(6, 6)
            .color(defaultTheme.getElementColorNew());
    menu.add(new ExitButton(this, this::onExit).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    menu.add(new Add().position(420, 530));
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
    maxScroll = componentCount * (SelectComponent.HEIGHT + SEPARATION) - 420;
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

  private void onExit() {
    Sorus.getSorus().getGUIManager().close(this.getParent());
    callback.cancel();
    Sorus.getSorus().getGUIManager().open(parent);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    main.onRemove();
    Sorus.getSorus().getSettingsManager().save();
    super.exit();
  }

  protected boolean shouldInitiateDrag(double mouseX, double mouseY) {
    IScreen screen = Sorus.getSorus().getVersion().getData(IScreen.class);
    double scaledMouseX = mouseX / screen.getScaledWidth() * 1920;
    double scaledMouseY = mouseY / screen.getScaledHeight() * 1080;
    return !(scaledMouseX > 510 && scaledMouseX < 1410 && scaledMouseY > 240 && scaledMouseY < 840);
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      callback.cancel();
      Sorus.getSorus().getGUIManager().close(this.getParent());
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
        DefaultSelectComponentScreen.this.setTargetScroll(
            -(initialLocation + delta / (420 * scrollBar.absoluteYScale()))
                * (DefaultSelectComponentScreen.this.maxScroll + 440));
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

  public void setSelected(SelectComponent selected) {
    this.selected = selected;
  }

  public SelectComponent getSelected() {
    return selected;
  }

  public class Add extends Collection {

    public Add() {
      Sorus.getSorus().getEventManager().register(this);
      Collection main;
      this.add(main = new Collection());
      main.add(
          new Rectangle()
              .smooth(4)
              .size(60, 60)
              .color(defaultTheme.getElementBackgroundColorNew()));
      Collection centerCross = new Collection();
      centerCross.add(new Rectangle().size(10, 40).position(25, 10));
      centerCross.add(new Rectangle().size(40, 10).position(10, 25));
      main.add(centerCross);
      main.attach(new Expand(100, 60, 60, 0.05));
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
        callback.call(DefaultSelectComponentScreen.this.selected.getComponent());
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
  }
}

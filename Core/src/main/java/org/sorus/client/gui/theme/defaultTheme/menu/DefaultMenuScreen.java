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

package org.sorus.client.gui.theme.defaultTheme.menu;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultMenuScreen extends ThemeBase<MenuScreen> {

  private static final double SEPARATION = 20;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  private int count;

  private final boolean performOpenAnimation;
  private long initTime;

  public DefaultMenuScreen(boolean performOpenAnimation) {
    this.performOpenAnimation = performOpenAnimation;
  }

  @Override
  public void init() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    this.main = new Panel();
    Collection menu = new Collection().position(510, 240);
    main.add(menu);
    this.addGuiPreComponents(menu);
    menu.add(new Scissor().size(870, 440).add(scroll = new Scroll()).position(0, 80));
    menu.add(scrollBar = new ScrollBar().position(870, 90));
    this.addGuiPostComponents(menu);
    Text title = new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).text("MODULES").scale(6, 6);
    menu.add(new ExitButton(this::onExit).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    this.addOption("HUDS", null, null);
    this.addOption("MODULES", null, () -> {
      Sorus.getSorus().getGUIManager().open(new ModuleListScreen());
    });
    this.addOption("THEMES", null, null);
    this.addOption("PLUGINS", null, null);
    this.addOption("COSMETICS", null, null);
    this.addOption("SETTINGS", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
    this.addOption("TEST", null, null);
  }

  private void addOption(String name, MenuComponent.ILogoCreator creator, Runnable runnable) {
    this.scroll.add(new MenuComponent(name, creator, runnable).position(SEPARATION + (int) (count % 4) * (MenuComponent.WIDTH + SEPARATION), SEPARATION + (int) (count / 4) * (MenuComponent.HEIGHT + SEPARATION)));
    count++;
  }

  private void addGuiPreComponents(Collection collection) {
    collection.add(new Rectangle().size(900, 600).smooth(10).color(DefaultTheme.getBackgroundColorNew()));
  }

  private void addGuiPostComponents(Collection collection) {
    collection.add(new Rectangle().size(900, 15).gradient(DefaultTheme.getGradientEndColorNew(), DefaultTheme.getGradientEndColorNew(), DefaultTheme.getGradientStartColorNew(), DefaultTheme.getGradientStartColorNew()).position(0, 70));
    collection.add(new Rectangle().size(900, 15).gradient(DefaultTheme.getGradientStartColorNew(), DefaultTheme.getGradientStartColorNew(), DefaultTheme.getGradientEndColorNew(), DefaultTheme.getGradientEndColorNew()).position(0, 515));
    collection.add(new Rectangle().size(900, 80).smooth(10).color(DefaultTheme.getForegroundColorNew()));
    collection.add(new Rectangle().size(900, 80).smooth(10).position(0, 520).color(DefaultTheme.getForegroundColorNew()));
  }

  @Override
  public void render() {
    int lineCount = (int) Math.ceil(scroll.getComponents().size() / 4.0);
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll = MathUtil.clamp(targetScroll + scrollValue * 0.7, scroll.getMinScroll(), scroll.getMaxScroll());
    currentScroll = (targetScroll - currentScroll) * 12 / FPS + scroll.getScroll();
    scroll.setScroll(currentScroll);
    maxScroll = lineCount * (MenuComponent.HEIGHT + SEPARATION) - 420;
    scroll.addMinMaxScroll(-maxScroll, 0);
    double size = Math.min(1, 440 / (maxScroll + 440));
    this.scrollBar.updateScrollBar(-currentScroll / (maxScroll + 440), size);
    main.scale(
            Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
            Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender(this.screen);
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    this.main.onRemove();
  }

  private void onExit() {
    Sorus.getSorus().getGUIManager().close(this.screen);
    Sorus.getSorus().getGUIManager().open(new HUDPositionScreen(false));
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  private void setTargetScroll(double targetScroll) {
    this.targetScroll = targetScroll;
  }

  public class ScrollBar extends Collection {

    private final Rectangle scrollBar;

    private double location, size;

    private boolean dragging;
    private double initialDragY;
    private double initialLocation;

    public ScrollBar() {
      this.add(new Rectangle().size(18, 420).smooth(9).color(DefaultTheme.getElementBackgroundColorNew()));
      this.add(scrollBar = new Rectangle().smooth(9).color(DefaultTheme.getElementMedgroundColorNew()));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      this.scrollBar.size(18, 420 * size).position(0, 420 * location);
      if(dragging) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        double delta = mouseY - initialDragY;
        DefaultMenuScreen.this.setTargetScroll(-(initialLocation + delta / (420 * scrollBar.absoluteYScale())) * (DefaultMenuScreen.this.maxScroll + 440));
      }
      super.onRender();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if(e.getX() > scrollBar.absoluteX() && e.getX() < scrollBar.absoluteX() + 18 * scrollBar.absoluteXScale() && e.getY() > scrollBar.absoluteY() && e.getY() < scrollBar.absoluteY() + 420 * size * scrollBar.absoluteYScale()) {
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

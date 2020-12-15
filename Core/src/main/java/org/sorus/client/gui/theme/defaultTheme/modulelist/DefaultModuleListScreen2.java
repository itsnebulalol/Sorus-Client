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

package org.sorus.client.gui.theme.defaultTheme.modulelist;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.MenuScreen;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.ModuleManager;
import org.sorus.client.util.ColorUtil;
import org.sorus.client.util.KeyHelper;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;
import org.sorus.client.version.render.IRenderer;

public class DefaultModuleListScreen2 extends ThemeBase<ModuleListScreen> {

  private static final double SEPARATION = 15;

  private final ModuleManager moduleManager;

  private Panel main;
  private Scroll scroll;
  private double targetScroll;
  private double currentScroll;
  private ScrollBar scrollBar;
  private double maxScroll;

  public DefaultModuleListScreen2(ModuleManager moduleManager) {
    this.moduleManager = moduleManager;
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
    Text title = new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).text("MODULES").scale(6, 6).color(DefaultTheme.getElementColorNew());
    menu.add(new ExitButton(this::onExit).position(10, 15));
    menu.add(title.position(450 - title.width() / 2 * 6, 30 - title.height() / 2 * 6));
    menu.add(new Search().position(20, 535));
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
    int moduleCount = scroll.getComponents().size();
    final int FPS = Math.max(Sorus.getSorus().getVersion().getData(IGame.class).getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getData(IInput.class).getScroll();
    targetScroll = MathUtil.clamp(targetScroll + scrollValue * 0.7, scroll.getMinScroll(), scroll.getMaxScroll());
    currentScroll = (targetScroll - currentScroll) * 12 / FPS + scroll.getScroll();
    scroll.setScroll(currentScroll);
    maxScroll = moduleCount * (ModuleListComponent2.HEIGHT + SEPARATION) - 420;
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

  private void onExit() {
    Sorus.getSorus().getGUIManager().close(this.screen);
    Sorus.getSorus().getGUIManager().open(new MenuScreen(false));
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      this.onExit();
    }
  }

  private void onSearchUpdate(String searchTerm) {
    this.scroll.clear();
    int i = 0;
    for(ModuleConfigurable module : moduleManager.getModules(ModuleConfigurable.class)) {
      if(searchTerm.isEmpty() || module.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
        this.scroll.add(new ModuleListComponent2(this, module).position(SEPARATION, SEPARATION + i * (ModuleListComponent2.HEIGHT + SEPARATION)));
        i++;
      }
    }
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
        DefaultModuleListScreen2.this.setTargetScroll(-(initialLocation + delta / (420 * scrollBar.absoluteYScale())) * (DefaultModuleListScreen2.this.maxScroll + 440));
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

  public class Search extends Collection {

    private final Rectangle rectangle;
    private final Collection collection;
    private final Text text;
    private final Image search;

    private boolean selected;

    private String message;

    private double selectedPercent;
    private long prevTime;

    public Search() {
      this.add(rectangle = new Rectangle().size(860, 50).smooth(10).color(DefaultTheme.getBackgroundColorNew()));
      this.add(collection = new Collection());
      collection.add(
              text =
                      new Text()
                              .fontRenderer(
                                      Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                              .scale(4, 4));
      collection.add(search = new Image().resource("sorus/modules/search_icon.png").size(30, 30).position(15, 10));
      this.message = "";
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long currentTime = System.currentTimeMillis();
      long deltaTime = currentTime - prevTime;
      selectedPercent = MathUtil.clamp(selectedPercent + (selected ? 1 : -1) * deltaTime * 0.015, 0, 1);
      collection.color(ColorUtil.getBetween(DefaultTheme.getElementSecondColorNew(), DefaultTheme.getElementColorNew(), selectedPercent));
      this.updateText(message);
      this.rectangle.onRender();
      this.search.onRender();
      Scissor.beginScissor(
              this.absoluteX() + 60 * this.absoluteXScale(),
              this.absoluteY(),
              790 * this.absoluteXScale(),
              50 * this.absoluteYScale());
      this.text.onRender();
      Scissor.endScissor();
      this.prevTime = currentTime;
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected =
              e.getX() > this.absoluteX()
                      && e.getX() < this.absoluteX() + 880 * this.absoluteXScale()
                      && e.getY() > this.absoluteY()
                      && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
    }

    @EventInvoked
    public void keyPressed(KeyPressEvent e) {
      if (selected) {
        switch (e.getKey()) {
          case BACKSPACE:
            if (!message.isEmpty()) {
              this.message = message.substring(0, message.length() - 1);
            }
            break;
          default:
            this.message = message + KeyHelper.getTypedChar(e.getKey());
            break;
        }
        DefaultModuleListScreen2.this.onSearchUpdate(this.message);
      }
    }

    private void updateText(String string) {
      this.text.text(string + " ");
      if (this.text.width() > 195) {
        this.text.position(
                840 - (this.text.width() * 4),
                25 - this.text.height() / 2 * 4);
      } else {
        this.text.position(60, 25 - this.text.height() / 2 * 4);
      }
      if (selected) {
        this.text.text(string + (System.currentTimeMillis() % 1000 > 500 ? "_" : ""));
      } else if(string.isEmpty()) {
        this.text.text("Search");
      }
    }

  }

}

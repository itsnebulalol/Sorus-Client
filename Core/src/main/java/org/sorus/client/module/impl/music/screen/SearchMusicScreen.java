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

package org.sorus.client.module.impl.music.screen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.ColorUtil;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.Key;

public class SearchMusicScreen extends Screen {

  private final ISearchProvider searchProvider;

  private Panel main;
  private Scroll scroll;

  private int resultCount;

  private double targetScroll;

  public SearchMusicScreen(ISearchProvider searchProvider) {
    this.searchProvider = searchProvider;
  }

  @Override
  public void onOpen() {
    main = new Panel();
    Collection menu = new Collection();
    menu.position(1520, 0);
    main.add(menu);
    menu.add(
        new Rectangle()
            .smooth(5)
            .size(405, 1080)
            .position(0, 0)
            .color(DefaultTheme.getBackgroundLayerColor()));
    menu.add(
        new Rectangle().size(405, 85).position(0, 5).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(180, 270)
            .position(0, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Arc()
            .radius(5, 5)
            .angle(90, 180)
            .position(390, 0)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle().size(390, 5).position(5, 0).color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(400, 7)
            .position(0, 90));
    menu.add(
        new Rectangle()
            .size(380, 130)
            .position(10, 935)
            .color(DefaultTheme.getMedgroundLayerColor()));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(380, 4)
            .position(10, 931));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(390, 931));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, 130)
            .position(390, 935));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, 4)
            .position(390, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(380, 4)
            .position(10, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(6, 1065));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 100)
            .position(6, 965));
    menu.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
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
            .color(DefaultTheme.getForegroundLayerColor()));
    menu.add(
        new ExitButton(
                () -> {
                  Sorus.getSorus().getGUIManager().close(this);
                  Sorus.getSorus().getGUIManager().open(new MainMusicScreen());
                })
            .position(10, 10));
    menu.add(new SearchBox("").position(10, 100));
    menu.add(new Scissor().size(380, 765).add(scroll = new Scroll()).position(10, 160));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void onRender() {
    final int FPS = Math.max(Sorus.getSorus().getVersion().getGame().getFPS(), 1);
    double scrollValue = Sorus.getSorus().getVersion().getInput().getScroll();
    targetScroll = targetScroll + scrollValue * 0.7;
    scroll.setScroll((targetScroll - scroll.getScroll()) * 7 / FPS + scroll.getScroll());
    double maxScroll = resultCount * 85 - 775;
    scroll.addMinMaxScroll(-maxScroll, 0);
    targetScroll = MathUtil.clamp(targetScroll, scroll.getMinScroll(), scroll.getMaxScroll());
    main.scale(
        Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void onExit() {
    Sorus.getSorus().getEventManager().unregister(this);
    this.main.onRemove();
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (e.getKey() == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this);
    }
  }

  private void search(String term) {
    new Thread(
            () -> {
              targetScroll = 0;
              scroll.clear();
              List<String> results = this.searchProvider.search(term);
              resultCount = 0;
              for (String string : results) {
                scroll.add(new SearchResult(string).position(0, resultCount * 85));
                resultCount++;
              }
            })
        .start();
  }

  @Override
  public boolean shouldTakeOutOfGame() {
    return true;
  }

  public class SearchResult extends Collection {

    private final String name;
    private boolean downloaded;

    public SearchResult(String name) {
      this.name = name;
      this.add(new Rectangle().size(380, 75).color(DefaultTheme.getMedgroundLayerColor()));
      this.add(new SelectButton().position(10, 45));
      this.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text(name)
              .scale(2, 2)
              .position(10, 13)
              .color(DefaultTheme.getForegroundLessLayerColor()));
    }

    public class SelectButton extends Collection {

      private double hoveredPercent;

      public SelectButton() {
        this.add(new Image().resource("sorus/modules/music/download.png").size(20, 20));
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRender() {
        boolean hovered =
            this.isHovered(
                Sorus.getSorus().getVersion().getInput().getMouseX(),
                Sorus.getSorus().getVersion().getInput().getMouseY());
        this.hoveredPercent = MathUtil.clamp(hoveredPercent + (hovered ? 1 : -1) * 0.05, 0, 1);
        this.color(
            ColorUtil.getBetween(
                DefaultTheme.getForegroundLessLayerColor(),
                DefaultTheme.getForegroundLayerColor(),
                hoveredPercent));
        super.onRender();
      }

      @Override
      public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
      }

      @EventInvoked
      public void onClick(MousePressEvent e) {
        if (this.isHovered(e.getX(), e.getY()) && !downloaded) {
          SearchMusicScreen.this.searchProvider.get(name);
          this.clear();
          this.add(new Image().resource("sorus/modules/music/check.png").size(20, 20));
          downloaded = true;
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

  public class SearchBox extends Collection {

    private final Rectangle rectangle;
    private final HollowRectangle hollowRectangle;
    private final Text text;

    private boolean selected;

    private String message;

    public SearchBox(String string) {
      this.add(rectangle = new Rectangle().size(380, 50));
      this.add(hollowRectangle = new HollowRectangle().size(380, 50));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                  .scale(4, 4));
      this.message = string;
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if (selected) {
        rectangle.color(DefaultTheme.getMedgroundLayerColor());
        hollowRectangle.color(DefaultTheme.getForegroundLayerColor());
        text.color(DefaultTheme.getForegroundLayerColor());
      } else {
        rectangle.color(DefaultTheme.getMedgroundLayerColor());
        hollowRectangle.color(DefaultTheme.getForegroundLessLayerColor());
        text.color(DefaultTheme.getForegroundLessLayerColor());
      }
      this.updateButton(message);
      this.rectangle.onRender();
      this.hollowRectangle.onRender();
      Sorus.getSorus()
          .getVersion()
          .getGLHelper()
          .beginScissor(
              this.absoluteX() + 10 * this.absoluteXScale(),
              this.absoluteY(),
              360 * this.absoluteXScale(),
              50 * this.absoluteYScale());
      this.text.onRender();
      Sorus.getSorus().getVersion().getGLHelper().endScissor();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected =
          e.getX() > this.absoluteX()
              && e.getX() < this.absoluteX() + 250 * this.absoluteXScale()
              && e.getY() > this.absoluteY()
              && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
    }

    @EventInvoked
    public void keyPressed(KeyPressEvent e) {
      if (selected) {
        char character = e.getCharacter();
        switch (e.getKey()) {
          case SHIFT_LEFT:
          case SHIFT_RIGHT:
            return;
          case BACKSPACE:
            if (!message.isEmpty()) {
              this.message = message.substring(0, message.length() - 1);
            }
            break;
          case ENTER:
            SearchMusicScreen.this.search(message);
            break;
          case V:
            if (Sorus.getSorus().getVersion().getInput().isKeyDown(Key.CONTROL_LEFT)) {
              Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
              Transferable t = c.getContents(this);
              if (t == null) {
                return;
              }
              try {
                this.message = message + t.getTransferData(DataFlavor.stringFlavor);
              } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
              }
              break;
            }
          default:
            this.message = message + character;
            break;
        }
      }
    }

    private void updateButton(String string) {
      this.text.text(string + " ");
      if (this.text.width() > 350 * this.absoluteXScale()) {
        this.text.position(
            10 + 350 - (this.text.width() * 1 / this.absoluteXScale()),
            25 - this.text.height() / 2 * 4);
      } else {
        this.text.position(10, 25 - this.text.height() / 2 * 4);
      }
      if (selected) {
        this.text.text(string + (System.currentTimeMillis() % 1000 > 500 ? "_" : ""));
      }
    }
  }
}

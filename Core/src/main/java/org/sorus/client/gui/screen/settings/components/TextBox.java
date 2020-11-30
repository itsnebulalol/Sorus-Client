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

package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Scissor;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;

public class TextBox extends Configurable {

  private final Setting<String> setting;

  public TextBox(Setting<String> setting, String description) {
    this.setting = setting;
    this.add(new TextBoxInner().position(400, 0));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 30)
            .color(new Color(170, 170, 170)));
  }

  @Override
  public double getHeight() {
    return 80;
  }

  public class TextBoxInner extends Collection {

    private final Rectangle rectangle;
    private final HollowRectangle hollowRectangle;
    private final Text text;

    private boolean selected;

    private String message;

    public TextBoxInner() {
      this.add(rectangle = new Rectangle().size(250, 50));
      this.add(hollowRectangle = new HollowRectangle().size(250, 50));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                  .scale(4, 4));
      this.message = setting.getValue();
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
      Scissor.beginScissor(
          this.absoluteX() + 10 * this.absoluteXScale(),
          this.absoluteY(),
          240 * this.absoluteXScale(),
          50 * this.absoluteYScale());
      this.text.onRender();
      Scissor.endScissor();
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
          default:
            this.message = message + character;
            break;
        }
        setting.setValue(this.message);
      }
    }

    private void updateButton(String string) {
      this.text.text(string + " ");
      if (this.text.width() > 220 * this.absoluteXScale()) {
        this.text.position(
            10 + 220 - (this.text.width() * 1 / this.absoluteXScale()),
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

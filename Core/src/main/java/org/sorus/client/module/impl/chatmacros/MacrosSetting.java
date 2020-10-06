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

package org.sorus.client.module.impl.chatmacros;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class MacrosSetting extends Configurable {

  private final Setting<Map<Input, String>> setting;

  private final List<MacroSetting> macroSettings = new ArrayList<>();

  private Add add;

  public MacrosSetting(Setting<Map<Input, String>> setting) {
    this.setting = setting;
    this.refresh();
  }

  @Override
  public void onRender() {
    this.add.position(325, this.macroSettings.size() * 80);
    super.onRender();
  }

  private void addMacro(int index) {
    MacroSetting macroSetting = new MacroSetting(index);
    this.add(macroSetting);
    macroSetting.position(0, macroSettings.size() * 80);
    this.macroSettings.add(macroSetting);
  }

  private void createMacro() {
    setting.getValue().put(Key.F, "Test");
    this.addMacro(Math.max(0, setting.getValue().size() - 1));
  }

  private void deleteMacro(int index) {
    this.setting.getValue().remove(this.macroSettings.get(index).input);
    this.refresh();
  }

  private void refresh() {
    this.clear();
    this.macroSettings.clear();
    for (int i = 0; i < setting.getValue().size(); i++) {
      this.addMacro(i);
    }
    this.add(add = new Add());
  }

  @Override
  public double getHeight() {
    return 100;
  }

  public class Add extends Collection {

    public Add() {
      this.add(new Rectangle().smooth(5).size(50, 50).color(new Color(15, 15, 15, 125)));
      this.add(
          new HollowRectangle()
              .thickness(2)
              .smooth(5)
              .size(50, 50)
              .color(new Color(235, 235, 235, 210)));
      this.add(
          new Rectangle()
              .smooth(3)
              .size(30, 10)
              .position(10, 20)
              .color(new Color(235, 235, 235, 210)));
      this.add(
          new Rectangle()
              .smooth(3)
              .size(10, 30)
              .position(20, 10)
              .color(new Color(235, 235, 235, 210)));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        MacrosSetting.this.createMacro();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 50 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 50 * this.absoluteYScale();
    }
  }

  public class MacroSetting extends Collection {

    private final Map<Input, String> macros;

    private int index;

    private Input input;
    private String string;

    public MacroSetting(int index) {
      this.setIndex(index);
      this.macros = MacrosSetting.this.setting.getValue();
      this.update();
      this.add(new KeybindInner().position(0, 5));
      this.add(new TextBoxInner().position(270, 5));
      this.add(new Remove().position(540, 5));
    }

    @Override
    public void onRender() {
      this.update();
      super.onRender();
    }

    private void update() {
      this.input = new ArrayList<>(macros.keySet()).get(index);
      string = macros.get(input);
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public void setInput(Input input) {
      this.macros.remove(this.input);
      this.macros.put(input, string);
      this.input = input;
    }

    public void setMessage(String message) {
      this.macros.replace(input, message);
    }

    public class Remove extends Collection {

      public Remove() {
        this.add(new Rectangle().smooth(5).size(50, 50).color(new Color(15, 15, 15, 125)));
        this.add(
            new HollowRectangle()
                .thickness(2)
                .smooth(5)
                .size(50, 50)
                .color(new Color(235, 235, 235, 210)));
        this.add(
            new Rectangle()
                .smooth(3)
                .size(30, 10)
                .position(10, 20)
                .color(new Color(235, 235, 235, 210)));
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
        super.onRemove();
      }

      @EventInvoked
      public void onClick(MousePressEvent e) {
        if (this.isHovered(e.getX(), e.getY())) {
          MacrosSetting.this.deleteMacro(index);
        }
      }

      private boolean isHovered(double x, double y) {
        return x > this.absoluteX()
            && x < this.absoluteX() + 50 * this.absoluteXScale()
            && y > this.absoluteY()
            && y < this.absoluteY() + 50 * this.absoluteYScale();
      }
    }

    public class KeybindInner extends Collection {

      private final Rectangle rectangle;
      private final HollowRectangle hollowRectangle;
      private final Text text;

      private boolean selected;

      public KeybindInner() {
        this.add(rectangle = new Rectangle().smooth(5).size(250, 50));
        this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5).size(250, 50));
        this.add(
            text =
                new Text()
                    .fontRenderer(
                        Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                    .scale(4, 4));
        this.updateButton(MacroSetting.this.input.getName());
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRender() {
        if (selected) {
          rectangle.color(new Color(15, 15, 15, 175));
          hollowRectangle.color(new Color(235, 235, 235, 255));
          text.color(new Color(235, 235, 235, 255));
          this.updateButton("?");
        } else {
          rectangle.color(new Color(15, 15, 15, 125));
          hollowRectangle.color(new Color(235, 235, 235, 210));
          text.color(new Color(235, 235, 235, 210));
        }
        super.onRender();
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
          MacroSetting.this.setInput(e.getKey());
          this.updateButton(e.getKey().getName());
          selected = false;
        }
      }

      private void updateButton(String string) {
        this.text.text(string);
        this.text.position(125 - this.text.width() / 2 * 4, 25 - this.text.height() / 2 * 4);
      }
    }

    public class TextBoxInner extends Collection {

      private final Rectangle rectangle;
      private final HollowRectangle hollowRectangle;
      private final Text text;

      private boolean selected;

      private String message;

      public TextBoxInner() {
        this.add(rectangle = new Rectangle().smooth(5).size(250, 50));
        this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5).size(250, 50));
        this.add(
            text =
                new Text()
                    .fontRenderer(
                        Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                    .scale(4, 4));
        this.message = string;
        Sorus.getSorus().getEventManager().register(this);
      }

      @Override
      public void onRender() {
        if (selected) {
          rectangle.color(new Color(15, 15, 15, 175));
          hollowRectangle.color(new Color(235, 235, 235, 255));
          text.color(new Color(235, 235, 235, 255));
        } else {
          rectangle.color(new Color(15, 15, 15, 125));
          hollowRectangle.color(new Color(235, 235, 235, 210));
          text.color(new Color(235, 235, 235, 210));
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
                240 * this.absoluteXScale(),
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
            default:
              this.message = message + character;
              break;
          }
          MacroSetting.this.setMessage(this.message);
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
}

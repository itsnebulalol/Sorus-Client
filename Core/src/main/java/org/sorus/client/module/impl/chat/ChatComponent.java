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

package org.sorus.client.module.impl.chat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.chat.ChatEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.MultiText;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Scroll;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class ChatComponent extends Component {

  private final Setting<Double> chatWidth;
  private final Setting<Double> maxLineCount;
  private final Setting<Color> backgroundColor;
  private final Setting<Input> chatKeybind;

  private final Panel modPanel;
  private final Scroll chatScroll;
  private final Rectangle background;

  private double chatScrollOffset;

  private final List<String> chat = new ArrayList<>();
  private final List<MultiText> chatComponents = new ArrayList<>();

  private double chatHeight;
  private double prevChatWidth;

  private boolean screenOpen;

  private double x, y;

  public ChatComponent() {
    super("CHAT");
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    this.register(chatWidth = new Setting<>("chatWidth", 150.0));
    this.register(maxLineCount = new Setting<>("maxLineCount", 25.0));
    this.register(chatKeybind = new Setting<>("chatKeybind", Key.I));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle().size(50, 50).color(Color.RED));
    modPanel.add(chatScroll = new Scroll());
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void render(double x, double y) {
    this.x = x;
    this.y = y;
    if (chatWidth.getValue() != prevChatWidth) {
      this.chatScroll.clear();
      this.chatHeight = 0;
      for (String string : this.chat) {
        double chatHeight = this.chatHeight;
        this.addChat(string, chatHeight);
      }
      prevChatWidth = chatWidth.getValue();
      this.recalculatePos();
    }
    this.background.size(this.getWidth(), this.getHeight()).color(this.backgroundColor.getValue());
    this.modPanel.position(x, y);
    this.background.onRender();
    Sorus.getSorus()
        .getVersion()
        .getGLHelper()
        .beginScissor(x, y, this.getWidth(), (this.getHeight() - 6) * this.getScale());
    for (int i = (int) ((-chatScrollOffset - chatScroll.getScroll()) / 4);
        i < chatComponents.size();
        i++) {
      chatComponents.get(i).onRender();
    }
    Sorus.getSorus().getVersion().getGLHelper().endScissor();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
  }

  @Override
  public double getWidth() {
    return this.chatWidth.getValue();
  }

  @Override
  public double getHeight() {
    return (screenOpen
            ? maxLineCount.getValue() * 5
            : Math.min(maxLineCount.getValue() * 5, chatHeight))
        + 7;
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Keybind(chatKeybind, "Chat Open Keybind"));
    collection.add(new Slider(chatWidth, 100, 200, "Chat Width"));
    collection.add(new Slider(maxLineCount, 5, 50, "Max Line Count"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  @EventInvoked
  public void onChat(ChatEvent e) {
    this.chat.add(e.getMessage());
    double chatHeight = this.chatHeight;
    this.addChat(e.getMessage(), chatHeight);
    this.recalculatePos();
  }

  private void addChat(String string, double y) {
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    double offsetY = 0;
    Color color = Color.WHITE;
    for (String chatLineString :
        this.getSplitString(
            fontRenderer, 1 / 0.5 * ChatComponent.this.chatWidth.getValue() - 25, string)) {
      MultiText multiText = new MultiText().scale(0.5, 0.5).position(0, offsetY + 2);
      boolean formattingSymbolFound = false;
      StringBuilder partialString = new StringBuilder();
      Text text = new Text().fontRenderer(fontRenderer);
      for (char c : chatLineString.toCharArray()) {
        if (formattingSymbolFound) {
          multiText.add(text.text(partialString.toString()).color(color));
          text = new Text().fontRenderer(fontRenderer);
          switch (c) {
            case '0':
              color = Color.BLACK;
              break;
            case '1':
              color = new Color(0, 0, 0, 170);
              break;
            case '2':
              color = new Color(0, 170, 0);
              break;
            case '3':
              color = new Color(0, 170, 170);
              break;
            case '4':
              color = new Color(170, 0, 0);
              break;
            case '5':
              color = new Color(170, 0, 170);
              break;
            case '6':
              color = new Color(255, 170, 0);
              break;
            case '7':
              color = new Color(170, 170, 170);
              break;
            case '8':
              color = new Color(85, 85, 85);
              break;
            case '9':
              color = new Color(85, 85, 255);
              break;
            case 'a':
              color = new Color(85, 255, 85);
              break;
            case 'b':
              color = new Color(85, 255, 255);
              break;
            case 'c':
              color = new Color(255, 85, 85);
              break;
            case 'd':
              color = new Color(255, 85, 255);
              break;
            case 'e':
              color = new Color(255, 255, 85);
              break;
            case 'f':
              color = new Color(255, 255, 255);
              break;
            case 'r':
              color = Color.WHITE;
              break;
          }
          formattingSymbolFound = false;
          partialString = new StringBuilder();
        } else if (c == '\u00a7') {
          formattingSymbolFound = true;
        } else {
          partialString.append(c);
        }
      }
      if (!partialString.toString().isEmpty()) {
        multiText.add(text.text(partialString.toString()).color(color));
      }
      multiText.position(2, y + offsetY + 2);
      this.chatScroll.add(multiText);
      this.chatComponents.add(multiText);
      offsetY += 4;
      chatHeight += 4;
    }
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (Sorus.getSorus().getVersion().getGame().isIngame()
        && e.getKey() == this.chatKeybind.getValue()
        && !e.isRepeat()) {
      Sorus.getSorus().getGUIManager().open(new ChatScreen(this));
    }
  }

  public double getInputX() {
    return this.x;
  }

  public double getInputY() {
    return this.y + (this.getHeight() - 7) * this.getScale();
  }

  public double getScale() {
    return super.getScale();
  }

  public Setting<Double> getChatWidth() {
    return chatWidth;
  }

  private void recalculatePos() {
    if (this.chatHeight > this.maxLineCount.getValue() * 5) {
      chatScrollOffset = this.maxLineCount.getValue() * 5 - this.chatHeight;
    } else {
      chatScrollOffset = 0;
    }
    this.chatScroll.position(0, chatScrollOffset);
  }

  public Scroll getChatScroll() {
    return chatScroll;
  }

  public void setScreenOpen(boolean screenOpen) {
    this.screenOpen = screenOpen;
  }

  public double getChatHeight() {
    return chatHeight;
  }

  private List<String> getSplitString(IFontRenderer fontRenderer, double width, String chat) {
    List<String> strings = new ArrayList<>();
    StringBuilder fullString = new StringBuilder();
    StringBuilder partialString = new StringBuilder();
    boolean colorCodeMark = false;
    for (char c : chat.toCharArray()) {
      fullString.append(c);
      if (c == '\u00a7') {
        colorCodeMark = true;
        continue;
      }
      if (colorCodeMark) {
        colorCodeMark = false;
        continue;
      }
      partialString.append(c);
      if (fontRenderer.getStringWidth(partialString.toString()) > width) {
        String string = fullString.toString();
        int index = string.lastIndexOf(" ");
        strings.add(string.substring(0, index));
        fullString = new StringBuilder(string.substring(index + 1));
        partialString = new StringBuilder(string.substring(index + 1));
      }
    }
    strings.add(fullString.toString());
    return strings;
  }
}

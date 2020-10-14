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

package org.sorus.client.module.impl.keystrokes;

import java.awt.*;
import java.util.Arrays;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.KeybindType;

public class KeystrokesComponent extends Component {

  private IFontRenderer fontRenderer;

  private final Setting<Long> mode;
  private final Setting<Double> fadeTime;
  private final Setting<Color> mainStandardColor;
  private final Setting<Color> mainPressedColor;
  private final Setting<Color> backgroundStandardColor;
  private final Setting<Color> backgroundPressedColor;
  private final Setting<Double> borderSize;
  private final Setting<Color> borderStandardColor;
  private final Setting<Color> borderPressedColor;
  private final Setting<Boolean> customFont;

  private final Panel modPanel;

  private Mode currentMode;

  public KeystrokesComponent() {
    super("KEYSTROKES");
    this.register(mode = new Setting<>("mode", 0L));
    this.register(fadeTime = new Setting<>("fadeTime", 0.0));
    this.register(mainStandardColor = new Setting<>("mainStandardColor", Color.WHITE));
    this.register(mainPressedColor = new Setting<>("mainPressedColor", Color.WHITE));
    this.register(
        backgroundStandardColor = new Setting<>("backgroundStandardColor", new Color(0, 0, 0, 50)));
    this.register(
        backgroundPressedColor = new Setting<>("backgroundPressedColor", new Color(0, 0, 0, 125)));
    this.register(borderSize = new Setting<>("borderSize", 1.0));
    this.register(
        borderStandardColor = new Setting<>("borderStandardColor", new Color(0, 0, 0, 0)));
    this.register(borderPressedColor = new Setting<>("borderPressedColor", new Color(0, 0, 0, 0)));
    this.register(customFont = new Setting<>("customFont", false));
    this.modPanel = new Panel();
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y) {
    this.updateFontRenderer();
    if (currentMode == null || mode.getValue() != currentMode.ordinal()) {
      this.modPanel.clear();
      this.currentMode = Mode.values()[mode.getValue().intValue()];
      this.modPanel.clear();
      for (KeystrokesKey.KeyData keyData : this.currentMode.getKeys()) {
        this.modPanel.add(new KeystrokesKey(this, keyData));
      }
    }
    this.modPanel.position(x, y);
    this.modPanel.onRender();
  }

  private void updateFontRenderer() {
    if (customFont.getValue()) {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    } else {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer();
    }
  }

  @Override
  public double getWidth() {
    if (currentMode == null) {
      return 0;
    }
    double max = 0;
    for (KeystrokesKey.KeyData key : currentMode.getKeys()) {
      max = Math.max(max, key.getX() + key.getWidth());
    }
    return max;
  }

  @Override
  public double getHeight() {
    if (currentMode == null) {
      return 0;
    }
    double max = 0;
    for (KeystrokesKey.KeyData key : currentMode.getKeys()) {
      max = Math.max(max, key.getY() + key.getHeight());
    }
    return max;
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, Arrays.asList("WASD", "WASD-Space"), "Mode"));
    collection.add(new Slider(fadeTime, 0, 5000, "Fade Time"));
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new ColorPicker(mainStandardColor, "Main Standard Color"));
    collection.add(new ColorPicker(mainPressedColor, "Main Pressed Color"));
    collection.add(new ColorPicker(backgroundStandardColor, "Background Standard Color"));
    collection.add(new ColorPicker(backgroundPressedColor, "Background Pressed Color"));
    collection.add(new Slider(borderSize, 0.5, 3, "Border Size"));
    collection.add(new ColorPicker(borderStandardColor, "Border Standard Color"));
    collection.add(new ColorPicker(borderPressedColor, "Border Pressed Color"));
  }

  @Override
  public String getDescription() {
    return "Displays currently pressed keys.";
  }

  public double getFadeTime() {
    return fadeTime.getValue();
  }

  public Color getMainStandardColor() {
    return mainStandardColor.getValue();
  }

  public Color getMainPressedColor() {
    return mainPressedColor.getValue();
  }

  public Color getBackgroundStandardColor() {
    return backgroundStandardColor.getValue();
  }

  public Color getBackgroundPressedColor() {
    return backgroundPressedColor.getValue();
  }

  public double getBorderSize() {
    return borderSize.getValue();
  }

  public Color getBorderStandardColor() {
    return borderStandardColor.getValue();
  }

  public Color getBorderPressedColor() {
    return borderPressedColor.getValue();
  }

  public IFontRenderer getFontRenderer() {
    return fontRenderer;
  }

  public enum Mode {
    WASD(
        new KeystrokesKey.Bind(KeybindType.FORWARD, 20, 0, 18, 18),
        new KeystrokesKey.Bind(KeybindType.LEFT, 0, 20, 18, 18),
        new KeystrokesKey.Bind(KeybindType.BACK, 20, 20, 18, 18),
        new KeystrokesKey.Bind(KeybindType.RIGHT, 40, 20, 18, 18)),
    WASD_SPACE(
        new KeystrokesKey.Bind(KeybindType.FORWARD, 20, 0, 18, 18),
        new KeystrokesKey.Bind(KeybindType.LEFT, 0, 20, 18, 18),
        new KeystrokesKey.Bind(KeybindType.BACK, 20, 20, 18, 18),
        new KeystrokesKey.Bind(KeybindType.RIGHT, 40, 20, 18, 18),
        new KeystrokesKey.Bind(KeybindType.JUMP, 0, 40, 58, 18));

    private final KeystrokesKey.KeyData[] keys;

    Mode(KeystrokesKey.KeyData... keys) {
      this.keys = keys;
    }

    public KeystrokesKey.KeyData[] getKeys() {
      return keys;
    }
  }
}

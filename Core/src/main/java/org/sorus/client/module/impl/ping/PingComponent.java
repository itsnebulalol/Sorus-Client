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

package org.sorus.client.module.impl.ping;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.MultiText;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;

public class PingComponent extends Component {

  private final List<PingMode> registeredModes = new ArrayList<>();
  private final List<String> registeredModeNames = new ArrayList<>();
  private PingMode currentMode;

  private IFontRenderer fontRenderer;

  private final Setting<Long> mode;
  private final Setting<Boolean> customFont;
  private final Setting<Boolean> tightFit;
  private final Setting<Color> backgroundColor;

  private final List<String> labelOptions = new ArrayList<>(Arrays.asList("<", "[", ":"));

  private final Panel modPanel;
  private final Rectangle background;
  private final MultiText pingText;
  private final List<Text> pingTexts = new ArrayList<>();

  private String pingString = "";

  public PingComponent() {
    super("Ping");
    this.register(new PingMode.LabelPreMode());
    this.register(new PingMode.LabelPostMode());
    this.register(new PingMode.CustomMode());
    this.register(
        mode =
            new Setting<Long>("mode", 0L) {
              @Override
              public void setValue(Long value) {
                PingComponent.this.setMode(registeredModes.get(value.intValue()));
                super.setValue(value);
              }
            });
    this.register(customFont = new Setting<>("customFont", false));
    this.register(tightFit = new Setting<>("tightFit", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(pingText = new MultiText());
    this.currentMode = this.registeredModes.get(0);
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y) {
    this.updateFontRenderer();
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    StringBuilder pingBuilder = new StringBuilder();
    int i = 0;
    /*List<Pair<String, Color>> formatted = this.currentMode.format(Sorus.getSorus().getVersion().getGame().getPing());
    for (Pair<String, Color> pair : formatted) {
      if(i >= this.pingTexts.size()) {
        Text text = new Text();
        this.pingTexts.add(text);
        this.pingText.add(text);
      }
      Text text = this.pingTexts.get(i);
      text.text(pair.getKey()).fontRenderer(fontRenderer).color(pair.getValue());
      i++;
      pingBuilder.append(pair.getLeft());
    }
    if(this.pingTexts.size() > formatted.size()) {
      Text text = pingTexts.get(pingTexts.size() - 1);
      pingTexts.remove(text);
      pingText.remove(text);
    }
    this.pingString = pingBuilder.toString();
    this.pingText.position(
            this.getWidth() / 2 - pingText.getWidth() / 2,
            this.getHeight() / 2 - pingText.getHeight() / 2);
    this.modPanel.position(x, y);
    this.modPanel.onRender();*/
  }

  private void updateFontRenderer() {
    if (customFont.getValue()) {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    } else {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer();
    }
  }

  public void register(PingMode mode) {
    this.registeredModes.add(mode);
    this.registeredModeNames.add(mode.getName());
  }

  public void setMode(PingMode mode) {
    if (this.currentMode != null) {
      for (Setting<?> setting : this.currentMode.getSettings()) {
        this.unregister(setting);
      }
    }
    this.currentMode = mode;
    for (Setting<?> setting : this.currentMode.getSettings()) {
      this.register(setting);
    }
    this.pingText.clear();
    this.pingTexts.clear();
    for (int i = 0; i < this.currentMode.format(0).size(); i++) {
      Text text = new Text();
      this.pingText.add(text);
      this.pingTexts.add(text);
    }
  }

  @Override
  public double getWidth() {
    return tightFit.getValue() ? fontRenderer.getStringWidth(pingString) + 4 : 60;
  }

  @Override
  public double getHeight() {
    return tightFit.getValue() ? fontRenderer.getFontHeight() + 4 : 11;
  }

  @Override
  public String getDescription() {
    return "Displays Ping.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, registeredModeNames, "Mode"));
    this.currentMode.addConfigComponents(collection);
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new Toggle(tightFit, "Tight Fit"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }
}

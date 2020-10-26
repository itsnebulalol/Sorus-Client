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

package org.sorus.client.module.impl.cps;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
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

public class CPSComponent extends Component {

  private final List<CPSMode> registeredModes = new ArrayList<>();
  private final List<String> registeredModeNames = new ArrayList<>();
  private CPSMode currentMode;

  private IFontRenderer fontRenderer;

  private final Setting<Long> mode;
  private final Setting<Boolean> customFont;
  private final Setting<Boolean> tightFit;
  private final Setting<Color> backgroundColor;

  private final Panel modPanel;
  private final Rectangle background;
  private final MultiText cpsText;
  private final List<Text> cpsTexts = new ArrayList<>();

  private final List<Long> prevClickTimes = new ArrayList<>();
  private String cpsString = "";

  public CPSComponent() {
    super("CPS");
    this.register(new CPSMode.LabelPreMode());
    this.register(new CPSMode.LabelPostMode());
    this.register(mode = new Setting<Long>("mode", 0L) {
      @Override
      public void setValue(Long value) {
        CPSComponent.this.setMode(registeredModes.get(value.intValue()));
        super.setValue(value);
      }
    });
    this.register(customFont = new Setting<>("customFont", false));
    this.register(tightFit = new Setting<>("tightFit", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(cpsText = new MultiText());
    this.currentMode = this.registeredModes.get(0);
    this.updateFontRenderer();
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void render(double x, double y) {
    this.updateFontRenderer();
    long currentTime = System.currentTimeMillis();
    prevClickTimes.removeIf((value) -> currentTime - value > 1000);
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    StringBuilder cpsBuilder = new StringBuilder();
    int i = 0;
    for (Pair<String, Color> pair :
            this.currentMode.format(prevClickTimes.size())) {
      Text text = this.cpsTexts.get(i);
      text.text(pair.getKey()).fontRenderer(fontRenderer).color(pair.getValue());
      i++;
      cpsBuilder.append(pair.getLeft());
    }
    this.cpsString = cpsBuilder.toString();
    this.cpsText.position(
        this.getWidth() / 2 - cpsText.getWidth() / 2,
        this.getHeight() / 2 - cpsText.getHeight() / 2);
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

  public void register(CPSMode mode) {
    this.registeredModes.add(mode);
    this.registeredModeNames.add(mode.getName());
  }

  public void setMode(CPSMode mode) {
    if(this.currentMode != null) {
      for(Setting<?> setting : this.currentMode.getSettings()) {
        this.unregister(setting);
      }
    }
    this.currentMode = mode;
    for (Setting<?> setting : this.currentMode.getSettings()) {
      this.register(setting);
    }
    this.cpsText.clear();
    this.cpsTexts.clear();
    for (int i = 0; i < this.currentMode.format(0).size(); i++) {
      Text text = new Text();
      this.cpsText.add(text);
      this.cpsTexts.add(text);
    }
  }

  @Override
  public double getWidth() {
    return tightFit.getValue() ? fontRenderer.getStringWidth(cpsString) + 4 : 60;
  }

  @Override
  public double getHeight() {
    return tightFit.getValue() ? fontRenderer.getFontHeight() + 4 : 11;
  }

  @Override
  public String getDescription() {
    return "Displays CPS.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, registeredModeNames, "Mode"));
    this.currentMode.addConfigComponents(collection);
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new Toggle(tightFit, "Tight Fit"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  @EventInvoked
  public void onMouseClick(MousePressEvent e) {
    long tick = System.currentTimeMillis();
    prevClickTimes.add(tick);
  }
}

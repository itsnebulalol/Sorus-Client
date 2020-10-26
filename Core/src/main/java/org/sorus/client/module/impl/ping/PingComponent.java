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
import org.sorus.client.util.ArrayUtil;

public class PingComponent extends Component {

  private IFontRenderer fontRenderer;

  private final Setting<Long> labelMode;
  private final Setting<Color> labelMainColor;
  private final Setting<Color> labelExtraColor;
  private final Setting<Color> valueColor;
  private final Setting<Boolean> customFont;
  private final Setting<Boolean> tightFit;
  private final Setting<Color> backgroundColor;

  private final List<String> labelOptions = new ArrayList<>(Arrays.asList("<", "[", ":"));

  private final Panel modPanel;
  private final Rectangle background;
  private final MultiText pingText;
  private final Text pingLabelExtraPre;
  private final Text pingLabel;
  private final Text pingLabelExtraPost;
  private final Text pingValue;

  private String[] pingString = new String[0];

  public PingComponent() {
    super("Ping");
    this.register(labelMode = new Setting<>("labelMode", 0L));
    this.register(labelMainColor = new Setting<>("labelMainColor", Color.WHITE));
    this.register(labelExtraColor = new Setting<>("labelExtraColor", Color.WHITE));
    this.register(valueColor = new Setting<>("valueColor", Color.WHITE));
    this.register(customFont = new Setting<>("customFont", false));
    this.register(tightFit = new Setting<>("tightFit", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(
        pingText =
            new MultiText()
                .add(pingLabelExtraPre = new Text().fontRenderer(fontRenderer))
                .add(pingLabel = new Text().fontRenderer(fontRenderer))
                .add(pingLabelExtraPost = new Text().fontRenderer(fontRenderer))
                .add(pingValue = new Text().fontRenderer(fontRenderer)));
    this.updateFontRenderer();
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void render(double x, double y) {
    this.updateFontRenderer();
    this.background
        .size(this.getWidth() + 4, this.getHeight() + 4)
        .position(-2, -2)
        .color(backgroundColor.getValue());
    this.pingString =
        new String[] {
          this.getLabelExtraPre(),
          "Ping",
          this.getLabelExtraPost(),
          String.valueOf(Sorus.getSorus().getVersion().getGame().getPing())
        };
    this.pingLabelExtraPre
        .text(pingString[0])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.pingLabel
        .text(pingString[1])
        .fontRenderer(fontRenderer)
        .color(this.labelMainColor.getValue());
    this.pingLabelExtraPost
        .text(pingString[2])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.pingValue.text(pingString[3]).fontRenderer(fontRenderer).color(this.valueColor.getValue());
    this.pingText.position(
        this.getWidth() / 2 - pingText.getWidth() / 2,
        this.getHeight() / 2 - pingText.getHeight() / 2);
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

  private String getLabelExtraPre() {
    switch (this.labelMode.getValue().intValue()) {
      case 0:
        return "<";
      case 1:
        return "[";
      case 2:
        return "";
    }
    return null;
  }

  private String getLabelExtraPost() {
    switch (this.labelMode.getValue().intValue()) {
      case 0:
        return "> ";
      case 1:
        return "] ";
      case 2:
        return ": ";
    }
    return null;
  }

  @Override
  public double getWidth() {
    return tightFit.getValue() ? fontRenderer.getStringWidth(ArrayUtil.concat(pingString)) : 50;
  }

  @Override
  public double getHeight() {
    return tightFit.getValue() ? fontRenderer.getFontHeight() : 7;
  }

  @Override
  public String getDescription() {
    return "Displays Ping.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new Toggle(tightFit, "Tight Fit"));
    collection.add(new ClickThrough(labelMode, labelOptions, "Label Mode"));
    collection.add(new ColorPicker(labelMainColor, "Label Color"));
    collection.add(new ColorPicker(labelExtraColor, "Label Extra Color"));
    collection.add(new ColorPicker(valueColor, "Value Color"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }
}

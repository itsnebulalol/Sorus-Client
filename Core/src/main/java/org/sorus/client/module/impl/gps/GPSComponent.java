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

package org.sorus.client.module.impl.gps;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.MultiTextComponent;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.ArrayUtil;
import org.sorus.client.version.game.IEntity;

public class GPSComponent extends Component {

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
  private final Text xLabelExtraPre;
  private final Text xLabel;
  private final Text xLabelExtraPost;
  private final Text xValue;
  private final Text yLabelExtraPre;
  private final Text yLabel;
  private final Text yLabelExtraPost;
  private final Text yValue;
  private final Text zLabelExtraPre;
  private final Text zLabel;
  private final Text zLabelExtraPost;
  private final Text zValue;

  private String[] xString = new String[0];
  private String[] yString = new String[0];
  private String[] zString = new String[0];

  public GPSComponent() {
    super("GPS");
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
        new MultiTextComponent()
            .add(xLabelExtraPre = new Text())
            .add(xLabel = new Text())
            .add(xLabelExtraPost = new Text())
            .add(xValue = new Text())
            .position(2, 2));
    modPanel.add(
        new MultiTextComponent()
            .add(yLabelExtraPre = new Text())
            .add(yLabel = new Text())
            .add(yLabelExtraPost = new Text())
            .add(yValue = new Text())
            .position(2, 12));
    modPanel.add(
        new MultiTextComponent()
            .add(zLabelExtraPre = new Text())
            .add(zLabel = new Text())
            .add(zLabelExtraPost = new Text())
            .add(zValue = new Text())
            .position(2, 22));
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y) {
    this.updateFontRenderer();
    IEntity player = Sorus.getSorus().getVersion().getGame().getPlayer();
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    this.xString =
        new String[] {
          this.getLabelExtraPre(),
          "X",
          this.getLabelExtraPost(),
          String.format("%.0f", player.getX())
        };
    this.xLabelExtraPre
        .text(xString[0])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.xLabel.text(xString[1]).fontRenderer(fontRenderer).color(this.labelMainColor.getValue());
    this.xLabelExtraPost
        .text(xString[2])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.xValue.text(xString[3]).fontRenderer(fontRenderer).color(this.valueColor.getValue());
    this.yString =
        new String[] {
          this.getLabelExtraPre(),
          "Y",
          this.getLabelExtraPost(),
          String.format("%.0f", player.getY())
        };
    this.yLabelExtraPre
        .text(yString[0])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.yLabel.text(yString[1]).fontRenderer(fontRenderer).color(this.labelMainColor.getValue());
    this.yLabelExtraPost
        .text(yString[2])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.yValue.text(yString[3]).fontRenderer(fontRenderer).color(this.valueColor.getValue());
    this.zString =
        new String[] {
          this.getLabelExtraPre(),
          "Z",
          this.getLabelExtraPost(),
          String.format("%.0f", player.getZ())
        };
    this.zLabelExtraPre
        .text(zString[0])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.zLabel.text(zString[1]).fontRenderer(fontRenderer).color(this.labelMainColor.getValue());
    this.zLabelExtraPost
        .text(zString[2])
        .fontRenderer(fontRenderer)
        .color(this.labelExtraColor.getValue());
    this.zValue.text(zString[3]).fontRenderer(fontRenderer).color(this.valueColor.getValue());
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
    return tightFit.getValue()
        ? Math.max(
                Math.max(
                    fontRenderer.getStringWidth(ArrayUtil.concat(xString)),
                    fontRenderer.getStringWidth(ArrayUtil.concat(yString))),
                fontRenderer.getStringWidth(ArrayUtil.concat(zString)))
            + 4
        : 60;
  }

  @Override
  public double getHeight() {
    return fontRenderer.getFontHeight() * 3 + 10;
  }

  @Override
  public String getDescription() {
    return "Displays current position.";
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

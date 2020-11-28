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

package org.sorus.client.module.impl.blockoverlay;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.IGLHelper;

public class BlockOverlay extends ModuleConfigurable {

  private final Setting<Double> thickness;
  private final Setting<Color> color;

  public BlockOverlay() {
    super("BLOCK OVERLAY");
    this.register(thickness = new Setting<>("borderThickness", 2.0));
    this.register(color = new Setting<>("color", Color.BLACK));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Slider(thickness, 1, 15, "Outline Thickness"));
    collection.add(new ColorPicker(color, "Outline Color"));
  }

  @EventInvoked
  public void onRenderBlockOverlay(RenderObjectEvent.BlockOverlay e) {
    if (this.isEnabled()) {
      Color color = this.getColor();
      Sorus.getSorus()
          .getVersion()
          .getData(IGLHelper.class)
          .color(
              color.getRed() / 255.0,
              color.getGreen() / 255.0,
              color.getBlue() / 255.0,
              color.getAlpha() / 255.0);
      Sorus.getSorus().getVersion().getData(IGLHelper.class).lineWidth(this.getBorderThickness());
    }
  }

  @EventInvoked
  public void onRenderText(RenderObjectEvent.Text e) {
    if (e.getText().contains("Copyright")) {
      e.setCancelled(true);
    }
  }

  public double getBorderThickness() {
    return thickness.getValue();
  }

  public Color getColor() {
    return color.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/blockoverlay/logo.png").size(80, 80).color(new Color(175, 175, 175)));
  }
}

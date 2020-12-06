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

package org.sorus.client.module.impl.challenges;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

import java.awt.*;

public class Challenges extends ModuleConfigurable {

  private final Setting<Boolean> noCrosshair;

  public Challenges() {
    super("Challenges");
    this.register(noCrosshair = new Setting<>("noCrosshair", false));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(noCrosshair, "No Crosshair"));
  }

  @EventInvoked
  public void onRenderCrosshair(RenderObjectEvent.Crosshair e) {
    if (this.shouldHideCrosshair()) {
      e.setCancelled(true);
    }
  }

  public boolean shouldHideCrosshair() {
    return this.isEnabled() && this.noCrosshair.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Minecraft too easy? Some challenges to make it a little harder.";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/challenges/logo.png").size(80, 80));
  }

}

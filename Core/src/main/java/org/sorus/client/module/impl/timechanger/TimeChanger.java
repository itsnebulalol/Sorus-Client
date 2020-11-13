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

package org.sorus.client.module.impl.timechanger;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class TimeChanger extends ModuleConfigurable {

  private final Setting<Double> time;

  public TimeChanger() {
    super("TIME CHANGER");
    this.register(time = new Setting<>("time", 0.25));
  }

  public long getTime() {
    return (long) (time.getValue() * 24000);
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(
        new Image()
            .resource("sorus/modules/timechanger/logo_moon.png")
            .size(50, 50)
            .position(30, 30));
    collection.add(new Image().resource("sorus/modules/timechanger/logo_sun.png").size(50, 50));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new TimeSlider(time, 0.25, 0.75, "Time"));
    super.addConfigComponents(collection);
  }

  @Override
  public String getDescription() {
    return "Change the sky to any time you want.";
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

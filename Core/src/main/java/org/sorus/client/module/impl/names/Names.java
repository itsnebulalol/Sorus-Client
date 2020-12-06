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

package org.sorus.client.module.impl.names;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class Names extends ModuleConfigurable {

  private final Setting<Boolean> selfNameTag;
  private final Setting<Boolean> customName;
  private final Setting<String> customNameText;
  private final Setting<Boolean> overrideNameTag;

  public Names() {
    super("Custom Name");
    this.register(selfNameTag = new Setting<>("selfNameTag", false));
    this.register(customName = new Setting<>("customName", false));
    this.register(customNameText = new Setting<>("customNameText", ""));
    this.register(overrideNameTag = new Setting<>("overrideNameTag", false));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(selfNameTag, "Show Own Name Tag"));
    collection.add(new Toggle(customName, "Custom Name"));
    collection.add(new TextBox(customNameText, "Custom Name"));
    collection.add(new Toggle(overrideNameTag, "Override Name Tag"));
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  public boolean renderSelfName() {
    return this.isEnabled() && selfNameTag.getValue();
  }

  public boolean customName() {
    return this.isEnabled() && customName.getValue();
  }

  public boolean overrideNameTag() {
    return this.overrideNameTag.getValue();
  }

  public String getCustomName() {
    return this.customNameText.getValue();
  }

  @Override
  public String getDescription() {
    return "Customize your name (locally) to be whoever you want!";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/names/logo.png").size(80, 80));
  }

}

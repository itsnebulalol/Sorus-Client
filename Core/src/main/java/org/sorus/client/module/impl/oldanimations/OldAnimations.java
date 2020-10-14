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

package org.sorus.client.module.impl.oldanimations;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;

public class OldAnimations extends ModuleConfigurable {

  private final Setting<Boolean> animInteractBreak;
  private final Setting<Boolean> redArmorFlashAttack;

  public OldAnimations() {
    super("OLD ANIMATIONS");
    this.register(animInteractBreak = new Setting<>("animInteractBreak", false));
    this.register(redArmorFlashAttack = new Setting<>("redArmorFlashAttack", false));
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/oldanimations/logo.png").size(95, 95));
  }

  @Override
  public String getDescription() {
    return "Various animation changes to make the 1.8.9 game feel like 1.7.10.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(animInteractBreak, "Interact and Break Animation").position(30, 30));
    collection.add(new Toggle(redArmorFlashAttack, "Red Armor on Damage").position(30, 100));
  }

  public boolean shouldShowAnimInteractBreak() {
    return this.isEnabled() && animInteractBreak.getValue();
  }

  public boolean shouldShowRedArmor() {
    return this.isEnabled() && redArmorFlashAttack.getValue();
  }
}

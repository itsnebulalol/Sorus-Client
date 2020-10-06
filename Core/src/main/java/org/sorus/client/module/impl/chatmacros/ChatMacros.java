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

package org.sorus.client.module.impl.chatmacros;

import java.util.LinkedHashMap;
import java.util.Map;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;
import org.sorus.client.version.input.Key;

public class ChatMacros extends ModuleConfigurable {

  private final Setting<Map<Input, String>> macros;

  public ChatMacros() {
    super("CHAT MACROS");
    this.register(macros = new Setting<>("macros", new LinkedHashMap<>()));
    this.macros.getValue().put(Key.U, "Hello World!");
    this.macros.getValue().put(Key.Y, "Hello World2!");
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    if (!Sorus.getSorus().getVersion().getGame().isIngame() || !this.isEnabled()) {
      return;
    }
    String string = macros.getValue().get(e.getKey());
    if (string != null) {
      Sorus.getSorus().getVersion().getGame().sendChatMessage(string);
    }
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new MacrosSetting(macros));
  }
}

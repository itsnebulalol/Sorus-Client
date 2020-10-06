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

package org.sorus.client.gui.screen;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;

public class MenuButton extends Collection {

  public MenuButton(String name) {
    Text text;
    this.add(
        text =
            new Text()
                .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                .text(name)
                .scale(3.5, 3.5)
                .color(new Color(215, 215, 215)));
    this.add(
        new Rectangle()
            .size(text.width() * 3.5 - 20, 1)
            .position(10, 25)
            .color(new Color(215, 215, 215)));
  }
}

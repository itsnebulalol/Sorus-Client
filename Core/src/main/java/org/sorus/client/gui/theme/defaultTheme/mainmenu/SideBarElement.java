package org.sorus.client.gui.theme.defaultTheme.mainmenu;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.modifier.impl.OnClick;
import org.sorus.client.gui.core.modifier.impl.Recolor;

public class SideBarElement extends Collection {

  public SideBarElement(DefaultMainMenuScreen theme, String imagePath, Runnable runnable) {
    double size = 1080.0 / DefaultMainMenuScreen.SIDEBAR_COUNT;
    this.add(
        new Rectangle()
            .size(size, size)
            .attach(
                new Recolor(
                    175,
                    size,
                    size,
                    theme.getDefaultTheme().getBackgroundColorNew(),
                    theme.getDefaultTheme().getElementBackgroundColorNew()))
            .attach(new OnClick(size, size, runnable)));
    this.add(
        new Image()
            .resource(imagePath)
            .size(size * 0.4, size * 0.4)
            .position(size * 0.3, size * 0.3)
            .color(theme.getDefaultTheme().getElementColorNew()));
  }
}

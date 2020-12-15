package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.version.IScreen;

public class DefaultSomethingScreen<T extends Screen> extends ThemeBase<T> {

    private Panel main;

    @Override
    public void init() {
        main = new Panel();
        Collection bar = new Collection().position(50, 50);
        bar.add(new Rectangle().size(100, 100));
        main.add(bar);
    }

    @Override
    public void render() {
        main.scale(
                Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
                Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
        main.onRender();
    }

}

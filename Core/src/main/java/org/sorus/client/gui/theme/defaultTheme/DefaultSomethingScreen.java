package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.modulelist.ModuleListScreen;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.version.IScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSomethingScreen<T extends Screen> extends ThemeBase<T> {

    private final List<DefaultSomethingComponent> components = new ArrayList<>();

    private final int prevIndex;
    private final int index;

    private Panel main;
    private Rectangle selector;

    private long initTime;

    public DefaultSomethingScreen(int prevIndex, int index) {
        this.prevIndex = prevIndex;
        this.index = index;
    }

    @Override
    public void init() {
        this.initTime = System.currentTimeMillis();
        main = new Panel();
        this.components.clear();
        this.components.add(new DefaultSomethingComponent(this, () -> {
            Sorus.getSorus().getGUIManager().open(new ModuleListScreen(index));
        }));
        this.components.add(new DefaultSomethingComponent(this, () -> {

        }));
        final double WIDTH = 80 + components.size() * 70;
        Collection bar = new Collection().position(960 - WIDTH / 2, 900);
        bar.add(new Rectangle().size(WIDTH, 70).smooth(10).color(DefaultTheme.getBackgroundColorNew()));
        bar.add(new Image().resource("sorus/logo.png").size(70, 70));
        int i = 0;
        for(DefaultSomethingComponent component : components) {
            bar.add(component.position(85 + i * 70, 12.5));
            i++;
        }
        bar.add(selector = new Rectangle().size(65, 70).color(new Color(255, 255, 255, 50)));
        main.add(bar);
    }

    @Override
    public void render() {
        selector.size(65 * ((index >= 0) ? 1 : 0), 70);
        double realLocation = prevIndex + (index - prevIndex) * Math.min(1, (System.currentTimeMillis() - initTime) / 300.0);
        selector.position(75 + Math.max(0, realLocation) * 70, 0);
        main.scale(
                Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
                Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
        main.onRender();
    }

    @Override
    public void exit() {
        this.main.onRemove();
    }
}

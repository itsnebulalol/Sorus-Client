package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.ConfigurableThemeBase;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;

import java.awt.*;

public class DefaultToggle extends ConfigurableThemeBase<Toggle> {

    private final Collection main;

    private final Setting<Boolean> setting;

    private boolean value;
    private double switchPercent;

    private final Rectangle background;
    private final Rectangle selector;

    private long prevRenderTime;

    public DefaultToggle(Collection main, Setting<Boolean> setting, String description) {
        this.main = main;
        this.setting = setting;
        this.value = setting.getValue();
        main.add(this.background = new Rectangle().size(80, 40).smooth(20).position(740, 15));
        this.selector = new Rectangle().size(30, 30).smooth(15).color(new Color(235, 235, 235));
        main.add(selector);
        main.add(
                new Text()
                        .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                        .text(description)
                        .scale(3.5, 3.5)
                        .position(30, 30)
                        .color(DefaultTheme.getElementColorNew()));
        Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void render() {
        long renderTime = System.currentTimeMillis();
        long deltaTime = renderTime - prevRenderTime;
        switchPercent = Math.max(0, Math.min(1, switchPercent + (value ? 1 : -1) * deltaTime * 0.007));
        this.background.color(
                new Color(
                        (int) (160 - switchPercent * 135),
                        (int) (35 + switchPercent * 125),
                        (int) (35 + switchPercent * 30)));
        this.selector.position(745 + 40 * switchPercent, 20);
        prevRenderTime = renderTime;
        main.onRender();
    }

    @Override
    public void exit() {
        Sorus.getSorus().getEventManager().unregister(this);
        main.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
        if (e.getX() > main.absoluteX() + 740 * main.absoluteXScale()
                && e.getX() < main.absoluteX() + 820 * main.absoluteXScale()
                && e.getY() > main.absoluteY() + 20 * main.absoluteYScale()
                && e.getY() < main.absoluteY() + 60 * main.absoluteYScale()) {
            this.value = !value;
            this.setting.setValue(value);
        }
    }

    @Override
    public double getHeight() {
        return 70;
    }
}

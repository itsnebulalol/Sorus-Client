package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;

public class DefaultSomethingComponent extends Collection {

    private final DefaultSomethingScreen<?> theme;
    private final Runnable runnable;

    public DefaultSomethingComponent(DefaultSomethingScreen<?> theme, Runnable runnable) {
        this.theme = theme;
        this.runnable = runnable;
        this.add(new Rectangle().size(45, 45).smooth(10).color(DefaultTheme.getForegroundColorNew()));
        Sorus.getSorus().getEventManager().register(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
        if(this.isHovered(e.getX(), e.getY())) {
            Sorus.getSorus().getGUIManager().close(theme.screen);
            runnable.run();
        }
    }

    @Override
    public void onRemove() {
        Sorus.getSorus().getEventManager().unregister(this);
        super.onRemove();
    }

    private boolean isHovered(double x, double y) {
        return x > this.absoluteX() && x < this.absoluteX() + 45 * this.absoluteXScale() && y > this.absoluteY() && y < this.absoluteY() + 45 * this.absoluteYScale();
    }

}

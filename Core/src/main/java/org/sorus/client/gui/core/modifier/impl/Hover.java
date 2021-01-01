package org.sorus.client.gui.core.modifier.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.modifier.Modifier;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public abstract class Hover extends Modifier<Component> {

    private final double time;
    protected final double width, height;

    protected double percent;

    public Hover(double time, double width, double height) {
        this.time = time;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onUpdate() {
        double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        boolean hovered = this.isHovered(mouseX, mouseY);
        percent = MathUtil.clamp(percent + (hovered ? 1 : -1) * 50 / time, 0, 1);
        this.onUpdated();
    }

    protected abstract void onUpdated();

    private boolean isHovered(double x, double y) {
        return x > component.absoluteX()
                && x < component.absoluteX() + width * component.absoluteXScale()
                && y > component.absoluteY()
                && y < component.absoluteY() + height * component.absoluteYScale();
    }

}

package org.sorus.client.gui.core.modifier.impl;

import org.sorus.client.util.ColorUtil;

import java.awt.*;

public class Recolor extends Hover {

    private final Color base, hovered;

    public Recolor(double time, double width, double height, Color base, Color hovered) {
        super(time, width, height);
        this.base = base;
        this.hovered = hovered;
    }

    @Override
    protected void onUpdated() {
        component.color(ColorUtil.getBetween(base, hovered, percent));
    }
}
package org.sorus.client.module.impl.customcrosshair;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.settings.Setting;

import java.awt.*;

public class CustomCrosshair extends ModuleConfigurable {

    private final Setting<Double> height;
    private final Setting<Double> width;
    private final Setting<Double> thickness;
    private final Setting<Color> color_v;
    private final Setting<Color> color_h;

    public CustomCrosshair() {
        super("CUSTOM CROSSHAIR");

        this.register(height = new Setting<>("height", 6.));
        this.register(width = new Setting<>("width", 6.));
        this.register(thickness = new Setting<>("thickness", 1.));
        this.register(color_v = new Setting<>("color_v", Color.white));
        this.register(color_h = new Setting<>("color_h", Color.white));

        Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void addConfigComponents(Collection collection) {
        super.addConfigComponents(collection);
        collection.add(new Slider(height, 1, 12, "Height"));
        collection.add(new Slider(width, 1, 12, "Width"));
        collection.add(new Slider(thickness, 1, 5, "Thickness"));
        collection.add(new ColorPicker(color_v, "Vertical Color"));
        collection.add(new ColorPicker(color_h, "Horizontal Color"));
    }
    @EventInvoked
    public void onRenderCrosshair(RenderObjectEvent.Crosshair e) {
        if (this.shouldHideCrosshair()) {
            //e.setCancelled(true);
            render();
        }
    }
    public boolean shouldHideCrosshair() {
        return this.isEnabled();
    }

    public void render() {

        Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                (Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 2),
                (Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 2) - (height.getValue() / 2) + 0.5,
                thickness.getValue(),
                height.getValue(),
                color_v.getValue()
        );

        Sorus.getSorus().getGUIManager().getRenderer().drawRect(
                (Sorus.getSorus().getVersion().getScreen().getScaledWidth() / 2) - (width.getValue() / 2) + 0.5,
                (Sorus.getSorus().getVersion().getScreen().getScaledHeight() / 2),
                width.getValue(),
                thickness.getValue(),
                color_h.getValue()
        );
    }
}

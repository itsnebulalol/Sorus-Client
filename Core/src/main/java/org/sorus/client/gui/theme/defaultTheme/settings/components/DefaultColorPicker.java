package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.ThemeableScreen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.settings.ConfigurableThemeBase;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;

import java.awt.*;

public class DefaultColorPicker extends ConfigurableThemeBase<ColorPicker> {

    private final Collection main;

    private final Setting<Color> setting;

    private final Rectangle colorViewer;

    public DefaultColorPicker(Collection main, Setting<Color> setting, String description) {
        this.main = main;
        this.setting = setting;
        main.clear();
        main.add(colorViewer = new Rectangle().size(30, 30).smooth(7).position(785, 25));
        main.add(
                new HollowRectangle()
                        .thickness(2)
                        .size(40, 40).smooth(10)
                        .position(780, 20)
                        .color(new Color(170, 170, 170)));
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
        this.colorViewer.color(this.getCompleteColor());
        main.onRender();
    }

    @Override
    public void exit() {
        Sorus.getSorus().getEventManager().unregister(this);
        main.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
        boolean expanded =
                e.getX() > main.absoluteX() && e.getX() < main.absoluteX() + 870 * main.absoluteXScale() && e.getY() > main.absoluteY()
                        && e.getY() < main.absoluteY() + this.getHeight() * main.absoluteYScale();
        if (expanded && main.getContainer().isTopLevel()) {
            Sorus.getSorus()
                    .getGUIManager()
                    .open(new ColorPickerScreen(this.setting.getValue(), new ColorCallback()));
        }
    }

    public class ColorCallback implements Callback<Color> {

        @Override
        public void call(Color selected) {
            DefaultColorPicker.this.setting.setValue(selected);
        }

        @Override
        public void cancel() {}
    }

    public Color getCompleteColor() {
        return setting.getValue();
    }

    public static class ColorPickerScreen extends ThemeableScreen {

        public ColorPickerScreen(Color color, Callback<Color> callback) {
            super(Sorus.getSorus().getThemeManager().getTheme("color-picker", color, callback));
        }

        @Override
        public boolean shouldOpenBlank() {
            return true;
        }
    }

    @Override
    public double getHeight() {
        return 70;
    }

}

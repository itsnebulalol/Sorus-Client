package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.ConfigurableThemeBase;
import org.sorus.client.gui.screen.settings.SettingsHolder;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.ColorUtil;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

import java.util.List;

public class DefaultClickThrough extends ConfigurableThemeBase<ClickThrough> {

    private final Collection main;

    private final Setting<Long> setting;
    private final List<String> options;

    private long value;

    public DefaultClickThrough(Collection main, Setting<Long> setting, List<String> options, String description) {
        this.main = main;
        this.setting = setting;
        this.options = options;
        this.value = setting.getValue();
        main.add(new ClickThroughInner().position(575, 5));
        main.add(
                new Text()
                        .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                        .text(description)
                        .scale(3.5, 3.5)
                        .position(30, 25)
                        .color(DefaultTheme.getElementColorNew()));
    }

    @Override
    public void render() {
        main.onRender();
    }

    private void setValue(long value) {
        this.value = value;
        if (value > this.options.size() - 1) {
            this.value = 0;
        }
        if (value < 0) {
            this.value = this.options.size() - 1;
        }
        this.setting.setValue(this.value);
        ((SettingsHolder) main.getParent().getParent()).refresh();
    }

    @Override
    public void exit() {
        main.onRemove();
    }

    @Override
    public double getHeight() {
        return 70;
    }

    public class ClickThroughInner extends Collection {

        private final Text currentText;
        private final Rectangle left;
        private final Rectangle right;

        private double leftHoveredPercent;
        private double rightHoveredPercent;

        private long prevRenderTime;

        public ClickThroughInner() {
            this.add(
                    new Rectangle().size(250, 50).smooth(25).color(DefaultTheme.getElementBackgroundColorNew()));
            this.add(
                    currentText =
                            new Text()
                                    .fontRenderer(
                                            Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                                    .scale(3, 3).color(DefaultTheme.getElementColorNew()));
            Collection left = new Collection().position(4, 4);
            left.add(this.left = new Rectangle().size(42, 42).smooth(21));
            left.add(new Text().text("<").fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).position(15, 7).scale(3, 3).color(DefaultTheme.getElementColorNew()));
            this.add(left);
            Collection right = new Collection().position(204, 4);
            right.add(this.right = new Rectangle().size(42, 42).smooth(21));
            right.add(new Text().text(">").fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).position(15, 7).scale(3, 3).color(DefaultTheme.getElementColorNew()));
            this.add(right);
            /*this.add(
                    leftArrow =
                            new Text()
                                    .fontRenderer(
                                            Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                                    .text("<")
                                    .scale(4, 4)
                                    .position(13, 7));
            this.add(
                    rightArrow =
                            new Text()
                                    .fontRenderer(
                                            Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                                    .text(">")
                                    .scale(4, 4)
                                    .position(225, 7));*/
            Sorus.getSorus().getEventManager().register(this);
        }

        @Override
        public void onRender() {
            currentText.text(DefaultClickThrough.this.options.get((int) DefaultClickThrough.this.value));
            currentText.position(
                    125 - currentText.width() / 2 * 3, 25 - currentText.height() / 2 * 3 + 1);
            long renderTime = System.currentTimeMillis();
            long deltaTime = renderTime - prevRenderTime;
            double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
            double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
            this.leftHoveredPercent =
                    MathUtil.clamp(
                            leftHoveredPercent + (this.leftHovered(mouseX, mouseY) ? 1 : -1) * deltaTime * 0.01,
                            0,
                            1);
            this.left.color(ColorUtil.getBetween(DefaultTheme.getElementMedgroundColorNew(), DefaultTheme.getElementForegroundColorNew(), leftHoveredPercent));
            this.rightHoveredPercent =
                    MathUtil.clamp(
                            rightHoveredPercent + (this.rightHovered(mouseX, mouseY) ? 1 : -1) * deltaTime * 0.01,
                            0,
                            1);
            this.right.color(ColorUtil.getBetween(DefaultTheme.getElementMedgroundColorNew(), DefaultTheme.getElementForegroundColorNew(), rightHoveredPercent));
            prevRenderTime = renderTime;
            super.onRender();
        }

        @Override
        public void onRemove() {
            Sorus.getSorus().getEventManager().unregister(this);
            super.onRemove();
        }

        @EventInvoked
        public void onClick(MousePressEvent e) {
            if (this.leftHovered(e.getX(), e.getY())) {
                DefaultClickThrough.this.setValue(value - 1);
            }
            if (this.rightHovered(e.getX(), e.getY())) {
                DefaultClickThrough.this.setValue(value + 1);
            }
        }

        private boolean leftHovered(double x, double y) {
            return x > this.absoluteX()
                    && x < this.absoluteX() + 44 * this.absoluteXScale()
                    && y > this.absoluteY()
                    && y < this.absoluteY() + 44 * this.absoluteYScale();
        }

        private boolean rightHovered(double x, double y) {
            return x > this.absoluteX() + 210 * this.absoluteXScale()
                    && x < this.absoluteX() + 254 * this.absoluteXScale()
                    && y > this.absoluteY()
                    && y < this.absoluteY() + 44 * this.absoluteYScale();
        }
    }

}

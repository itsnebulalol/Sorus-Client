package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Arc;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.IConfigurableTheme;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.DefaultThemeBase;
import org.sorus.client.settings.Setting;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class DefaultSlider extends DefaultThemeBase<Slider> implements IConfigurableTheme {

  private final Collection main;
  private final Setting<Double> setting;
  private final double minValue, maxValue;

  private boolean selected;

  protected double value;

  private final Arc selector;
  private final Rectangle selector2;

  public DefaultSlider(
      DefaultTheme theme,
      Collection main,
      Setting<Double> setting,
      double minValue,
      double maxValue,
      String description) {
    super(theme);
    this.main = main;
    this.setting = setting;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.value = setting.getValue();
    main.add(
        new Rectangle()
            .smooth(3.5)
            .size(160, 7)
            .position(670, 36.5)
            .color(defaultTheme.getElementMedgroundColorNew()));
    main.add(
        this.selector2 =
            new Rectangle()
                .smooth(3.5)
                .position(670, 36.5)
                .color(defaultTheme.getElementBackgroundColorNew()));
    main.add(
        this.selector =
            new Arc()
                .angle(0, 360)
                .radius(10, 10)
                .color(defaultTheme.getElementBackgroundColorNew()));
    main.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 30)
            .color(defaultTheme.getElementColorNew()));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
    if (this.selected) {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double baseValue =
          (MathUtil.clamp(
                      mouseX,
                      main.absoluteX() + 670 * main.absoluteXScale(),
                      main.absoluteX() + 830 * main.absoluteXScale())
                  - (main.absoluteX() + 670 * main.absoluteXScale()))
              / (160 * main.absoluteXScale());
      value = minValue + (baseValue * (maxValue - minValue));
      this.setting.setValue(value);
    }
    this.selector.position(670 + ((value - minValue) / (maxValue - minValue)) * 160 - 10, 30);
    this.selector2.size(((value - minValue) / (maxValue - minValue)) * 160, 7);
    main.onRender();
  }

  @Override
  public void exit() {
    main.onRemove();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (main.getContainer().isTopLevel()) {
      selected = this.isHovered(e.getX(), e.getY());
    }
  }

  @EventInvoked
  public void onRelease(MouseReleaseEvent e) {
    selected = false;
  }

  private boolean isHovered(double x, double y) {
    return x > main.absoluteX() + 670 * main.absoluteXScale()
        && x < main.absoluteX() + 830 * main.absoluteXScale()
        && y > main.absoluteY() + 20 * main.absoluteYScale()
        && y < main.absoluteY() + 60 * main.absoluteYScale();
  }

  @Override
  public double getHeight() {
    return 80;
  }
}

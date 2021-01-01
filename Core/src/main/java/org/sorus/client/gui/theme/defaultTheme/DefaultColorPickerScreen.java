package org.sorus.client.gui.theme.defaultTheme;

import java.awt.*;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.modifier.impl.OnClick;
import org.sorus.client.gui.core.modifier.impl.Recolor;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.gui.theme.defaultTheme.settings.components.DefaultColorPicker;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class DefaultColorPickerScreen extends ThemeBase<DefaultColorPicker.ColorPickerScreen> {

  private final Color color;
  private final Callback<Color> callback;
  private Panel main;

  private ColorPickerInner colorPicker;
  private AlphaSlider alphaSlider;
  private ColorSlider colorSlider;

  public DefaultColorPickerScreen(Color color, Callback<Color> callback) {
    this.color = color;
    this.callback = callback;
  }

  @Override
  public void init() {
    this.main = new Panel(this.getParent());
    float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
    main.add(new Rectangle().size(320, 335).smooth(10).position(800, 350).color(DefaultTheme.getForegroundColorNew()));
    main.add(colorSlider = new ColorSlider(hsb[0]).position(1030, 370));
    main.add(alphaSlider = new AlphaSlider(color.getAlpha() / 255.0).position(1075, 370));
    main.add(
        colorPicker =
            new ColorPickerInner(
                    hsb[1], hsb[2])
                .position(815, 370));
    main.add(new DoneButton().position(1005, 630));
    this.updateSetting();
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender();
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.getParent());
    }
  }

  public void updateSetting() {
    this.colorPicker.update(
        Color.getHSBColor((float) this.colorSlider.getValue(), 1, 1));
    this.alphaSlider.update(this.colorPicker.getColor());
    this.callback.call(this.getCompleteColor());
  }

  public Color getCompleteColor() {
    Color color = this.colorPicker.getColor();
    return new Color(
        color.getRed(),
        color.getGreen(),
        color.getBlue(),
        (int) (this.alphaSlider.getValue() * 255));
  }

  public class ColorPickerInner extends Collection {

    private final HollowRectangle selector;
    private final Rectangle picker;

    private boolean selected;

    private double hue;
    private double xPercent, yPercent;

    public ColorPickerInner(double xPercent, double yPercent) {
      this.xPercent = xPercent;
      this.yPercent = yPercent;
      this.add(
          picker =
              new Rectangle()
                      .size(200, 200));
      this.add(selector = new HollowRectangle().size(10, 10).smooth(5));
      Sorus.getSorus().getEventManager().register(this);
      this.selector.position(xPercent * 200 - 5, (1 - yPercent) * 200 - 5);
    }

    @Override
    public void onRender() {
      double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
      double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
      if (selected) {
        double imageMouseX = (mouseX - this.absoluteX()) * 200 / (200 * this.absoluteXScale());
        double imageMouseY = (mouseY - this.absoluteY()) * 200 / (200 * this.absoluteYScale());
        imageMouseX = MathUtil.clamp(imageMouseX, 0, 199);
        imageMouseY = MathUtil.clamp(imageMouseY, 0, 199);
        selector.position(imageMouseX - 5, imageMouseY - 5);
        this.xPercent = imageMouseX / 200;
        this.yPercent = 1 - imageMouseY / 200;
        DefaultColorPickerScreen.this.updateSetting();
      }
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected =
          e.getX() > this.absoluteX()
              && e.getX() < this.absoluteX() + 200 * this.absoluteXScale()
              && e.getY() > this.absoluteY()
              && e.getY() < this.absoluteY() + 200 * this.absoluteYScale();
    }

    @EventInvoked
    public void onRelease(MouseReleaseEvent e) {
      selected = false;
    }

    public Color getColor() {
      return Color.getHSBColor((float) hue, (float) xPercent, (float) yPercent);
    }

    public void update(Color color) {
      this.hue = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
      this.picker.gradient(Color.BLACK, Color.BLACK, color, Color.WHITE);
    }
  }

  public class AlphaSlider extends Collection {

    private final Rectangle overlay;
    private final Rectangle selector;

    private boolean selected;
    private double value;

    public AlphaSlider(double value) {
      this.value = 1 - value;
      Sorus.getSorus().getEventManager().register(this);
      this.add(
          new Image()
              .resource("sorus/alpha-slider.png")
              .size(30, 200));
      this.add(overlay = new Rectangle().size(30, 200));
      this.add(selector = new HollowRectangle().thickness(1.5).size(30, 8).smooth(3).color(Color.WHITE));
    }

    @Override
    public void onRender() {
      if (selected) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        this.value =
            MathUtil.clamp((mouseY - this.absoluteY()) / (200 * this.absoluteYScale()), 0, 1);
        DefaultColorPickerScreen.this.updateSetting();
      }
      this.selector.position(0, 200 * value - 4);
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected = this.isHovered(e.getX(), e.getY());
    }

    @EventInvoked
    public void onRelease(MouseReleaseEvent e) {
      selected = false;
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 200 * this.absoluteYScale();
    }

    private void update(Color color) {
      Color noAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 25);
      overlay.gradient(noAlpha, noAlpha, color, color);
    }

    public double getValue() {
      return 1 - value;
    }
  }

  public class ColorSlider extends Collection {

    private final Rectangle selector;

    private boolean selected;
    private double value;

    public ColorSlider(double value) {
      this.value = value;
      Sorus.getSorus().getEventManager().register(this);
      this.add(new Image().resource("sorus/color-slider.png").size(30, 200));
      this.add(selector = new HollowRectangle().thickness(1.5).size(30, 8).smooth(3).color(Color.WHITE));
    }

    @Override
    public void onRender() {
      if (selected) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        this.value =
            MathUtil.clamp((mouseY - this.absoluteY()) / (200 * this.absoluteYScale()), 0, 1);
        DefaultColorPickerScreen.this.updateSetting();
      }
      this.selector.position(0, 200 * value - 4);
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected = this.isHovered(e.getX(), e.getY());
    }

    @EventInvoked
    public void onRelease(MouseReleaseEvent e) {
      selected = false;
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 200 * this.absoluteYScale();
    }

    public double getValue() {
      return value;
    }
  }

  public class DoneButton extends Collection {

    public DoneButton() {
      this.add(new Rectangle().size(100, 40).smooth(20).attach(new Recolor(100, 100, 40, DefaultTheme.getElementMedgroundColorNew(), DefaultTheme.getElementForegroundColorNew())));
      this.add(new Text().fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRawLineFontRenderer()).text("Done").position(23, 11).scale(2.5, 2.5).color(DefaultTheme.getElementColorNew()));
      this.attach(new OnClick(100, 40, () -> {
        Sorus.getSorus().getGUIManager().close(DefaultColorPickerScreen.this.getParent());
        callback.cancel();
      }));
    }

  }

}

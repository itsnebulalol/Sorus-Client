/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.sorus.client.gui.theme.defaultTheme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.theme.ExitButton;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class DefaultColorPickerScreen extends ThemeBase<ColorPicker.ColorPickerScreen> {

  private final Color color;
  private final Callback<Color> callback;
  private Panel main;

  private ColorPickerInner colorPicker;
  private AlphaSlider alphaSlider;
  private BrightnessSlider brightnessSlider;
  private Rectangle colorViewer;

  public DefaultColorPickerScreen(Color color, Callback<Color> callback) {
    this.color = color;
    this.callback = callback;
  }

  @Override
  public void init() {
    this.main = new Panel();
    main.add(new Rectangle().size(290, 335).position(815, 350).color(new Color(42, 42, 42)));
    main.add(
        new ExitButton(
                () -> {
                  Sorus.getSorus().getGUIManager().close(this.screen);
                  callback.cancel();
                })
            .position(815, 355));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30))
            .size(290, 4)
            .position(815, 346));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30))
            .size(4, 4)
            .position(1150, 346));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150))
            .size(4, 125)
            .position(1105, 395));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150))
            .size(4, 4)
            .position(1105, 685));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 150))
            .size(290, 4)
            .position(815, 685));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30))
            .size(4, 4)
            .position(811, 685));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30))
            .size(4, 335)
            .position(811, 350));
    main.add(
        new Rectangle()
            .gradient(
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 150),
                new Color(26, 26, 26, 30),
                new Color(26, 26, 26, 30))
            .size(4, 4)
            .position(811, 346));
    main.add(alphaSlider = new AlphaSlider(color.getAlpha() / 255.0).position(1045, 415));
    int max = Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
    double brightness = max / 255.0;
    main.add(brightnessSlider = new BrightnessSlider(brightness).position(835, 625));
    main.add(
        colorPicker =
            new ColorPickerInner(
                    new Color(
                        (int) (color.getRed() * (1 / brightness)),
                        (int) (color.getGreen() * (1 / brightness)),
                        (int) (color.getBlue() * (1 / brightness))))
                .position(835, 415));
    this.updateSetting();
    main.add(colorViewer = new Rectangle().size(40, 40).position(1045, 625));
    main.add(
        new HollowRectangle()
            .thickness(2)
            .size(40, 40)
            .position(1045, 625)
            .color(new Color(200, 200, 200, 210)));
  }

  @Override
  public void render() {
    this.colorViewer.color(this.getCompleteColor());
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    main.onRender(this.screen);
  }

  @Override
  public void keyTyped(Key key, boolean repeat) {
    if (key == Key.ESCAPE) {
      Sorus.getSorus().getGUIManager().close(this.screen);
    }
  }

  public void updateSetting() {
    this.colorPicker.updateColorWheel(
        this.brightnessSlider.getValue(), this.alphaSlider.getValue());
    this.callback.call(this.getCompleteColor());
  }

  public Color getCompleteColor() {
    Color color = this.colorPicker.getColor();
    double brightness = this.brightnessSlider.getValue();
    return new Color(
        (int) (color.getRed() * brightness),
        (int) (color.getGreen() * brightness),
        (int) (color.getBlue() * brightness),
        (int) (this.alphaSlider.getValue() * 255));
  }

  public class ColorPickerInner extends Collection {

    private final HollowRectangle selector;
    private final org.sorus.client.gui.core.component.impl.Image imageComponent;

    private BufferedImage image;

    private boolean selected;

    private Color color;

    public ColorPickerInner(Color color) {
      this.color = color;
      this.add(
          imageComponent =
              new org.sorus.client.gui.core.component.impl.Image()
                  .resource("sorus/color-picker.png")
                  .size(200, 200));
      this.add(
          new HollowRectangle().thickness(2).size(200, 200).color(new Color(200, 200, 200, 210)));
      this.add(selector = new HollowRectangle().thickness(2).size(10, 10));
      URL url =
          ColorPicker.class.getClassLoader().getResource("assets/minecraft/sorus/color-picker.png");
      try {
        this.image = ImageIO.read(Objects.requireNonNull(url));
      } catch (IOException e) {
        e.printStackTrace();
      }
      Sorus.getSorus().getEventManager().register(this);
      for (int i = 0; i < image.getWidth(); i++) {
        for (int j = 0; j < image.getHeight(); j++) {
          if (this.colorsEqual(new Color(image.getRGB(i, j)), color)) {
            this.selector.position(i - 5, j - 5);
            return;
          }
        }
      }
    }

    private boolean colorsEqual(Color color1, Color color2) {
      return Math.abs(color1.getRed() - color2.getRed())
              + Math.abs(color1.getGreen() - color2.getGreen())
              + Math.abs(color1.getBlue() - color2.getBlue())
          < 30;
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
        this.color = new Color(image.getRGB((int) imageMouseX, (int) imageMouseY));
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
      return color;
    }

    public void updateColorWheel(double brightness, double alpha) {
      int brightnessValue = (int) (brightness * 255);
      int alphaValue = (int) (alpha * 255);
      this.imageComponent.color(
          new Color(brightnessValue, brightnessValue, brightnessValue, alphaValue));
    }
  }

  public class AlphaSlider extends Collection {

    private final Rectangle selector;

    private boolean selected;
    private double value;

    public AlphaSlider(double value) {
      this.value = value;
      Sorus.getSorus().getEventManager().register(this);
      this.add(
          new org.sorus.client.gui.core.component.impl.Image()
              .resource("sorus/alpha-slider.png")
              .size(40, 200));
      this.add(
          new HollowRectangle().thickness(2).size(40, 200).color(new Color(200, 200, 200, 210)));
      this.add(selector = new Rectangle().size(40, 2).color(new Color(200, 200, 200, 210)));
    }

    @Override
    public void onRender() {
      if (selected) {
        double mouseY = Sorus.getSorus().getVersion().getData(IInput.class).getMouseY();
        this.value =
            MathUtil.clamp((mouseY - this.absoluteY()) / (200 * this.absoluteYScale()), 0, 1);
        DefaultColorPickerScreen.this.updateSetting();
      }
      this.selector.position(0, 200 * value - 1);
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

  public class BrightnessSlider extends Collection {

    private final Rectangle selector;

    private boolean selected;
    private double value;

    public BrightnessSlider(double value) {
      this.value = value;
      Sorus.getSorus().getEventManager().register(this);
      this.add(new Image().resource("sorus/brightness-slider.png").size(200, 40));
      this.add(
          new HollowRectangle().thickness(2).size(200, 40).color(new Color(200, 200, 200, 210)));
      this.add(selector = new Rectangle().size(2, 40).color(new Color(200, 200, 200, 210)));
    }

    @Override
    public void onRender() {
      if (selected) {
        double mouseX = Sorus.getSorus().getVersion().getData(IInput.class).getMouseX();
        this.value =
            MathUtil.clamp((mouseX - this.absoluteX()) / (200 * this.absoluteXScale()), 0, 1);
        DefaultColorPickerScreen.this.updateSetting();
      }
      this.selector.position(200 * value - 1, 0);
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
          && x < this.absoluteX() + 200 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }

    public double getValue() {
      return value;
    }
  }
}

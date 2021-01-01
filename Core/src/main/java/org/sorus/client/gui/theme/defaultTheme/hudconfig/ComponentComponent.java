package org.sorus.client.gui.theme.defaultTheme.hudconfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.util.Axis;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

public class ComponentComponent extends Collection {

  public static final double WIDTH = 410, HEIGHT = 110;

  private final DefaultHUDConfigScreen theme;
  private final Component component;

  public ComponentComponent(DefaultHUDConfigScreen theme, Component component) {
    this.theme = theme;
    this.component = component;
    final double ROUNDING = 10;
    this.add(
            new Rectangle()
                    .size(WIDTH, HEIGHT)
                    .smooth(ROUNDING)
                    .color(DefaultTheme.getForegroundColorNew()));
    this.add(
            new HollowRectangle()
                    .thickness(2)
                    .size(WIDTH, HEIGHT)
                    .smooth(ROUNDING).color(DefaultTheme.getElementMedgroundColorNew()));
    this.add(new Image().resource("sorus/modules/test_icon.png").size(70, 70).position(20, 20));
    this.add(
            new Text()
                    .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                    .text(component.getName())
                    .scale(3, 3)
                    .position(110, 25)
                    .color(DefaultTheme.getElementColorNew()));
    this.add(new Remove().position(WIDTH - 50, 10));
    this.add(new Settings().position(WIDTH - 50, HEIGHT - 50));
    Sorus.getSorus().getEventManager().register(this);
  }

  public List<String> getSplitDescription(
          String description, IFontRenderer fontRenderer, double width) {
    List<String> strings = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    for (char c : description.toCharArray()) {
      stringBuilder.append(c);
      if (fontRenderer.getStringWidth(stringBuilder.toString()) > width) {
        String string = stringBuilder.toString();
        int index = string.lastIndexOf(" ");
        strings.add(string.substring(0, index));
        stringBuilder = new StringBuilder(string.substring(index + 1));
      }
    }
    strings.add(stringBuilder.toString());
    return strings;
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
    super.onRemove();
  }

  public class Settings extends Collection {

    private final Collection main;
    private final Rectangle rectangle;
    private final Image image;
    private double hoverPercent;
    private long prevRenderTime;

    public Settings() {
      this.add(main = new Collection());
      main.add(
              rectangle =
                      new Rectangle().size(40, 40).smooth(20).color(DefaultTheme.getBackgroundColorNew()));
      main.add(
              image =
                      new Image()
                              .resource("sorus/gear.png")
                              .size(25, 25)
                              .position(7.5, 7.5)
                              .color(DefaultTheme.getElementColorNew()));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
      boolean hovered = this.isHovered(input.getMouseX(), input.getMouseY());
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      hoverPercent = MathUtil.clamp(hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
      main.position(-hoverPercent, -hoverPercent)
              .scale(1 + hoverPercent * 0.05, 1 + hoverPercent * 0.05);
      this.prevRenderTime = renderTime;
      rectangle.onRender();
      IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
      double x = main.absoluteX() + 20 * main.absoluteXScale(),
              y = main.absoluteY() + 20 * main.absoluteYScale();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, hoverPercent * 40);
      glHelper.translate(-x, -y, 0);
      image.onRender();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, -hoverPercent * 40);
      glHelper.translate(-x, -y, 0);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        Sorus.getSorus()
                .getGUIManager()
                .close(ComponentComponent.this.theme.getParent());
        Sorus.getSorus().getGUIManager().open(new SettingsScreen(theme.getParent(), component));
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
              && x < this.absoluteX() + 40 * this.absoluteXScale()
              && y > this.absoluteY()
              && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class Remove extends Collection {

    private final Collection main;
    private double hoverPercent;
    private long prevRenderTime;

    public Remove() {
      this.add(main = new Collection());
      main.add(new Rectangle().size(40, 40).smooth(20).color(DefaultTheme.getBackgroundColorNew()));
      main.add(
              new Image()
                      .resource("sorus/huds/trash_can.png")
                      .size(22.5, 25)
                      .position(8.25, 7.5)
                      .color(new Color(170, 30, 30)));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
      boolean hovered = this.isHovered(input.getMouseX(), input.getMouseY());
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      hoverPercent = MathUtil.clamp(hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
      main.position(-hoverPercent, -hoverPercent)
              .scale(1 + hoverPercent * 0.05, 1 + hoverPercent * 0.05);
      this.prevRenderTime = renderTime;
      main.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        component.getHUD().removeComponent(component);
        ComponentComponent.this.theme.updateComponents();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
              && x < this.absoluteX() + 40 * this.absoluteXScale()
              && y > this.absoluteY()
              && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public Component getComponent() {
    return component;
  }
}

package org.sorus.client.gui.theme.defaultTheme.modulelist;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.impl.*;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.screen.settings.SettingsScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.util.Axis;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

public class ModuleListComponent2 extends Collection {

  public static final double WIDTH = 410, HEIGHT = 110;

  private final DefaultModuleListScreen2 moduleListScreenTheme;
  private final ModuleConfigurable module;

  public ModuleListComponent2(
      DefaultModuleListScreen2 moduleListScreenTheme, ModuleConfigurable module) {
    this.moduleListScreenTheme = moduleListScreenTheme;
    this.module = module;
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
                .smooth(ROUNDING)
                .color(DefaultTheme.getElementMedgroundColorNew()));
    this.add(new Image().resource("sorus/modules/test_icon.png").size(70, 70).position(20, 20));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
            .text(module.getName())
            .scale(3, 3)
            .position(110, 25)
            .color(DefaultTheme.getElementColorNew()));
    this.add(new Toggle().position(WIDTH - 50, 10));
    this.add(new Settings().position(WIDTH - 50, HEIGHT - 50));
  }

  @Override
  public void onRender() {
    if (this.module.isEnabled()) {
      this.color(Color.WHITE);
      // border.color(new Color(67, 144, 32));
    } else {
      this.color(new Color(255, 255, 255, 122));
      // border.color(new Color(170, 29, 29));
    }
    super.onRender();
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
        Screen current = ModuleListComponent2.this.moduleListScreenTheme.getParent();
        Sorus.getSorus().getGUIManager().close(current);
        Sorus.getSorus()
            .getGUIManager()
            .open(new SettingsScreen(current, ModuleListComponent2.this.module));
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class Toggle extends Collection {

    private final Collection main;
    private Component icon;

    private double expandedPercent;
    private long prevRenderTime;

    public Toggle() {
      this.add(main = new Collection());
      main.add(new Rectangle().size(40, 40).smooth(20).color(DefaultTheme.getBackgroundColorNew()));
      Sorus.getSorus().getEventManager().register(this);
    }

    private void updateIcon(boolean hovered) {
      if (icon != null) {
        main.remove(icon);
      }
      if (hovered) {
        Color newElementColor = DefaultTheme.getElementColorNew();
        icon =
            new Rectangle()
                .size(25, 25)
                .smooth(12.5)
                .position(7.5, 7.5)
                .color(
                    new Color(
                        newElementColor.getRed(),
                        newElementColor.getGreen(),
                        newElementColor.getBlue(),
                        75));
      } else {
        if (ModuleListComponent2.this.module.isEnabled()) {
          icon =
              new Rectangle()
                  .size(25, 25)
                  .smooth(12.5)
                  .position(7.5, 7.5)
                  .color(DefaultTheme.getElementColorNew());
        } else {
          icon =
              new HollowRectangle()
                  .thickness(2)
                  .size(25, 25)
                  .smooth(12.5)
                  .position(7.5, 7.5)
                  .color(DefaultTheme.getElementColorNew());
        }
      }
      main.add(icon);
    }

    @Override
    public void onRender() {
      IInput input = Sorus.getSorus().getVersion().getData(IInput.class);
      boolean hovered = this.isHovered(input.getMouseX(), input.getMouseY());
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      expandedPercent =
          MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.01, 0, 1);
      main.position(-expandedPercent, -expandedPercent)
          .scale(1 + expandedPercent * 0.05, 1 + expandedPercent * 0.05);
      this.prevRenderTime = renderTime;
      this.updateIcon(hovered);
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (this.isHovered(e.getX(), e.getY())) {
        ModuleListComponent2.this.module.setEnabled(!ModuleListComponent2.this.module.isEnabled());
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }
}

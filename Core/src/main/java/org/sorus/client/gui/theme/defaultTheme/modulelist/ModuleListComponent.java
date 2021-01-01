package org.sorus.client.gui.theme.defaultTheme.modulelist;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.util.Axis;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.input.IInput;

public class ModuleListComponent extends Collection {

  private final DefaultModuleListScreen moduleListScreenTheme;

  public ModuleListComponent(
      DefaultModuleListScreen moduleListScreenTheme, ModuleConfigurable module) {
    this.moduleListScreenTheme = moduleListScreenTheme;
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer();
    final double WIDTH = 670;
    final double HEIGHT = 100;
    this.add(
        new Rectangle()
            .size(WIDTH, HEIGHT)
            .position(4, 4)
            .color(DefaultTheme.getMedbackgroundLayerColor()));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(WIDTH, 4)
            .position(4, 0));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(WIDTH + 4, 0));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, HEIGHT)
            .position(WIDTH + 4, 4));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor())
            .size(4, 4)
            .position(WIDTH + 4, HEIGHT + 4));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor())
            .size(WIDTH, 4)
            .position(4, HEIGHT + 4));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4)
            .position(0, HEIGHT + 4));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, HEIGHT)
            .position(0, 4));
    this.add(
        new Rectangle()
            .gradient(
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowStartColor(),
                DefaultTheme.getShadowEndColor(),
                DefaultTheme.getShadowEndColor())
            .size(4, 4));
    Collection collection = new Collection().position(15, 15);
    this.add(collection);
    module.addIconElements(collection);
    this.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(module.getName())
            .position(105, 15)
            .scale(4, 4)
            .color(DefaultTheme.getForegroundLessLayerColor()));
    int i = 0;
    for (String string :
        module.getSplitDescription(
            Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer(), 150)) {
      this.add(
          new Text()
              .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
              .text(string)
              .position(105, 50 + i * 18)
              .scale(2, 2)
              .color(DefaultTheme.getForegroundLessLessLayerColor()));
      i++;
    }
    this.add(new ToggleButton(module).position(515, 32.5));
    this.add(new SettingsButton(module).position(615, 32.5));
  }

  public static class ToggleButton extends Collection {

    private final ModuleConfigurable module;

    private boolean value;
    private double switchPercent;

    private final Rectangle background;
    private final Rectangle selector;

    private long prevRenderTime;

    public ToggleButton(ModuleConfigurable module) {
      this.module = module;
      this.value = module.isEnabled();
      this.background = new Rectangle().size(80, 45).smooth(22.5).position(0, 0);
      this.add(background);
      this.selector = new Rectangle().size(35, 35).smooth(17.5).color(new Color(235, 235, 235));
      this.add(selector);
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      switchPercent =
          Math.max(0, Math.min(1, switchPercent + (value ? 1 : -1) * deltaTime * 0.007));
      this.background.color(
          new Color(
              (int) (160 - switchPercent * 135),
              (int) (35 + switchPercent * 125),
              (int) (35 + switchPercent * 30)));
      this.selector.position(5 + 35 * switchPercent, 5);
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
      if (e.getX() > this.absoluteX()
          && e.getX() < this.absoluteX() + 80 * this.absoluteXScale()
          && e.getY() > this.absoluteY()
          && e.getY() < this.absoluteY() + 45 * this.absoluteYScale()) {
        this.value = !value;
        this.module.setEnabled(value);
      }
    }
  }

  public class SettingsButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final ModuleConfigurable module;

    private final Image image;

    public SettingsButton(ModuleConfigurable module) {
      this.module = module;
      this.add(image = new Image().resource("sorus/gear.png").size(45, 45));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          module.isEnabled()
              && this.isHovered(
                  Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
                  Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
      image.scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
      image.position(-2 * hoverPercent, -2 * hoverPercent);
      image.color(new Color(235, 235, 235, (int) (210 + 45 * hoverPercent)));
      prevRenderTime = renderTime;
      double x = this.absoluteX() + 22.5 * this.absoluteXScale();
      double y = this.absoluteY() + 22.5 * this.absoluteYScale();
      IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, hoverPercent * 50);
      glHelper.translate(-x, -y, 0);
      super.onRender();
      glHelper.translate(x, y, 0);
      glHelper.rotate(Axis.Z, -hoverPercent * 50);
      glHelper.translate(-x, -y, 0);
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
      super.onRemove();
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (module.isEnabled() && this.isHovered(e.getX(), e.getY())) {
        ModuleListComponent.this.moduleListScreenTheme.getParent().displayModuleSettings(module);
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 50 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 50 * this.absoluteYScale();
    }
  }
}

package org.sorus.client.gui.theme.defaultTheme.profilelist;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.modifier.impl.Expand;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.input.IInput;

public class ProfileListComponent extends Collection {

  public static final double WIDTH = 835, HEIGHT = 70;

  private final DefaultProfileListScreen theme;
  private final String profile;
  private final boolean selected;
  private final HollowRectangle border;

  public ProfileListComponent(DefaultProfileListScreen theme, String profile) {
    this.theme = theme;
    this.profile = profile;
    this.selected = Sorus.getSorus().getSettingsManager().getCurrentProfile().equals(profile);
    final double ROUNDING = 10;
    this.add(
        new Rectangle()
            .size(WIDTH, HEIGHT)
            .smooth(ROUNDING)
            .color(theme.getDefaultTheme().getForegroundColorNew()));
    this.add(
        border =
            new HollowRectangle()
                .thickness(2)
                .size(WIDTH, HEIGHT)
                .smooth(ROUNDING)
                .color(
                    selected
                        ? theme.getDefaultTheme().getElementSecondColorNew()
                        : theme.getDefaultTheme().getElementMedgroundColorNew()));
    this.add(
        new Image()
            .resource("sorus/modules/test_icon.png")
            .size(45, 45)
            .position(12.5, 12.5)
            .color(theme.getDefaultTheme().getElementColorNew()));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
            .text(profile)
            .scale(3, 3)
            .position(80, 22)
            .color(theme.getDefaultTheme().getElementColorNew()));
    /*if (Sorus.getSorus().getSettingsManager().getCurrentProfile().equals(this.profile)) {
      this.add(
          new Rectangle()
              .size(200, 5)
              .position(125, 90)
              .color(theme.getDefaultTheme().getForegroundLessLayerColor()));
    }*/
    this.add(new Remove().position(WIDTH - 100, HEIGHT / 2 - 20));
    this.add(new LoadButton().position(WIDTH - 50, HEIGHT / 2 - 20));
  }

  public class Remove extends Collection {

    private final Collection main;
    private double hoverPercent;
    private long prevRenderTime;

    public Remove() {
      this.add(main = new Collection());
      main.add(
          new Rectangle()
              .size(40, 40)
              .smooth(20)
              .color(theme.getDefaultTheme().getBackgroundColorNew()));
      main.add(
          new Image()
              .resource("sorus/huds/trash_can.png")
              .size(22.5, 25)
              .position(8.25, 7.5)
              .color(selected ? new Color(35, 35, 35) : new Color(170, 30, 30)));
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
      if (!selected && this.isHovered(e.getX(), e.getY())) {
        File file = new File("sorus/settings/" + profile);
        if (file.exists()) {
          try {
            FileUtils.deleteDirectory(file);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
        theme.updateProfiles();
      }
    }

    private boolean isHovered(double x, double y) {
      return x > this.absoluteX()
          && x < this.absoluteX() + 40 * this.absoluteXScale()
          && y > this.absoluteY()
          && y < this.absoluteY() + 40 * this.absoluteYScale();
    }
  }

  public class LoadButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    public LoadButton() {
      Collection main;
      this.add(main = new Collection());
      main.add(
          new Rectangle()
              .size(40, 40)
              .smooth(20)
              .color(theme.getDefaultTheme().getBackgroundColorNew()));
      main.attach(new Expand(100, 40, 40, 0.05));
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      long renderTime = System.currentTimeMillis();
      long deltaTime = renderTime - prevRenderTime;
      boolean hovered =
          this.isHovered(
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseX(),
              Sorus.getSorus().getVersion().getData(IInput.class).getMouseY());
      hoverPercent =
          Math.max(0, Math.min(1, hoverPercent + (hovered ? 1 : -1) * deltaTime * 0.008));
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
      if (this.isHovered(e.getX(), e.getY())) {
        ProfileListComponent.this.theme.getParent().loadProfile(ProfileListComponent.this.profile);
        ProfileListComponent.this.theme.updateProfiles();
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

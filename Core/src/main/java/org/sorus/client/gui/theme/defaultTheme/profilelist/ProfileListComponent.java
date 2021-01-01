package org.sorus.client.gui.theme.defaultTheme.profilelist;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.input.IInput;

public class ProfileListComponent extends Collection {

  private final DefaultProfileListScreen profileListScreenTheme;
  private final String profile;

  public ProfileListComponent(DefaultProfileListScreen profileListScreenTheme, String profile) {
    this.profileListScreenTheme = profileListScreenTheme;
    this.profile = profile;
    IFontRenderer fontRenderer =
        Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    final double WIDTH = 685;
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
    this.add(
        new Text()
            .fontRenderer(fontRenderer)
            .text(profile)
            .position(125, 20)
            .scale(4, 4)
            .color(DefaultTheme.getForegroundLessLayerColor()));
    if (Sorus.getSorus().getSettingsManager().getCurrentProfile().equals(this.profile)) {
      this.add(
          new Rectangle()
              .size(200, 5)
              .position(125, 90)
              .color(DefaultTheme.getForegroundLessLayerColor()));
    }
    this.add(new LoadButton().position(615, 15));
  }

  public class LoadButton extends Collection {

    private double hoverPercent;

    private long prevRenderTime;

    private final Rectangle rectangle;

    public LoadButton() {
      this.add(rectangle = new Rectangle().size(50, 50));
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
      rectangle.scale(1 + hoverPercent * 0.1, 1 + hoverPercent * 0.1);
      rectangle.position(-2 * hoverPercent, -2 * hoverPercent);
      rectangle.color(new Color(235, 235, 235, (int) (210 + 45 * hoverPercent)));
      prevRenderTime = renderTime;
      double x = this.absoluteX() + 20 * this.absoluteXScale();
      double y = this.absoluteY() + 20 * this.absoluteYScale();
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
        ProfileListComponent.this
            .profileListScreenTheme
            .getParent()
            .loadProfile(ProfileListComponent.this.profile);
        ProfileListComponent.this.profileListScreenTheme.updateProfiles();
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

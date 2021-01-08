package org.sorus.client.gui.theme.defaultTheme.mainmenu;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;

public abstract class MainMenuBottomElement extends Collection {

  protected final Collection collection = new Collection();
  private long prevRenderTime;
  private double expandedPercent;

  public MainMenuBottomElement() {
    collection.add(new Rectangle().size(75, 75).smooth(37.5).color(Color.GREEN));
    this.add(collection);
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
    expandedPercent =
        MathUtil.clamp(expandedPercent + (hovered ? 1 : -1) * deltaTime * 0.015, 0, 1);
    collection
        .position(-expandedPercent * 2, -expandedPercent * 2)
        .scale(1 + 0.05 * expandedPercent, 1 + 0.05 * expandedPercent);
    prevRenderTime = renderTime;
    super.onRender();
  }

  @Override
  public void onRemove() {
    Sorus.getSorus().getEventManager().unregister(this);
    super.onRemove();
  }

  private boolean isHovered(double x, double y) {
    return x > this.absoluteX()
        && x < this.absoluteX() + 75 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 75 * this.absoluteYScale();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      this.onClick();
    }
  }

  public abstract void onClick();

  public static class LanguagesButton extends MainMenuBottomElement {

    public LanguagesButton() {
      collection.add(
          new Image()
              .resource("sorus/modules/custommenus/earth.png")
              .size(50, 50)
              .position(12.5, 12.5));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.LANGUAGES);
    }
  }

  public static class SettingsButton extends MainMenuBottomElement {

    public SettingsButton() {
      collection.add(
          new Image()
              .resource("sorus/gear.png")
              .size(50, 50)
              .position(12.5, 12.5)
              .color(Color.BLACK));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.SETTINGS);
    }
  }

  public static class ExitButton extends MainMenuBottomElement {

    public ExitButton() {
      collection.add(
          new Image()
              .resource("sorus/modules/custommenus/exit.png")
              .size(50, 50)
              .position(12.5, 12.5)
              .color(Color.BLACK));
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).shutdown();
    }
  }
}

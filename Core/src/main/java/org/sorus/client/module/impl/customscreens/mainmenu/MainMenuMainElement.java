package org.sorus.client.module.impl.customscreens.mainmenu;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.util.MathUtil;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;

public abstract class MainMenuMainElement extends Collection {

  private final Collection collection = new Collection();
  private long prevRenderTime;
  private double expandedPercent;

  public MainMenuMainElement(String name, String image) {
    collection.add(new Rectangle().size(450, 90).color(Color.BLUE));
    Text text;
    collection.add(
        text =
            new Text()
                .text(name)
                .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                .scale(4, 4)
                .color(Color.BLUE));
    text.position(225 - text.width() * 4 / 2 - 15, 45 - text.height() * 4 / 2);
    collection.add(
        new Image()
            .resource(image)
            .size(60, 60)
            .position(225 + text.width() * 4 / 2 - 10, 17.5)
            .color(Color.BLUE));
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
        .position(-expandedPercent * 2, -expandedPercent * 0.5)
        .scale(1 + 0.01 * expandedPercent, 1 + 0.01 * expandedPercent);
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
        && x < this.absoluteX() + 450 * this.absoluteXScale()
        && y > this.absoluteY()
        && y < this.absoluteY() + 90 * this.absoluteYScale();
  }

  @EventInvoked
  public void onClick(MousePressEvent e) {
    if (this.isHovered(e.getX(), e.getY())) {
      this.onClick();
    }
  }

  public abstract void onClick();

  public static class Singleplayer extends MainMenuMainElement {

    public Singleplayer() {
      super("Singleplayer", "sorus/modules/custommenus/solo.png");
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.VIEW_WORLDS);
    }
  }

  public static class Multiplayer extends MainMenuMainElement {

    public Multiplayer() {
      super("Multiplayer", "sorus/modules/custommenus/multi.png");
    }

    @Override
    public void onClick() {
      Sorus.getSorus().getGUIManager().close((Screen) this.getContainer());
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.VIEW_SERVERS);
    }
  }
}

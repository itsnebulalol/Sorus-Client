package org.sorus.client.gui.screen.settings.components;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.Configurable;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;

public class Keybind extends Configurable {

  private final Setting<Input> setting;

  public Keybind(Setting<Input> setting, String description) {
    this.setting = setting;
    this.add(new KeybindInner().position(400, 25));
    this.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 40)
            .color(new Color(170, 170, 170)));
  }

  @Override
  public double getHeight() {
    return 90;
  }

  public class KeybindInner extends Collection {

    private final Rectangle rectangle;
    private final HollowRectangle hollowRectangle;
    private final Text text;

    private boolean selected;

    public KeybindInner() {
      this.add(rectangle = new Rectangle().smooth(5).size(250, 50));
      this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5).size(250, 50));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .scale(4, 4));
      this.updateButton(Keybind.this.setting.getValue().getName());
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if (selected) {
        rectangle.color(new Color(15, 15, 15, 175));
        hollowRectangle.color(new Color(235, 235, 235, 255));
        text.color(new Color(235, 235, 235, 255));
        this.updateButton("?");
      } else {
        rectangle.color(new Color(15, 15, 15, 125));
        hollowRectangle.color(new Color(235, 235, 235, 210));
        text.color(new Color(235, 235, 235, 210));
      }
      super.onRender();
    }

    @Override
    public void onRemove() {
      Sorus.getSorus().getEventManager().unregister(this);
    }

    @EventInvoked
    public void onClick(MousePressEvent e) {
      selected =
          e.getX() > this.absoluteX()
              && e.getX() < this.absoluteX() + 250 * this.absoluteXScale()
              && e.getY() > this.absoluteY()
              && e.getY() < this.absoluteY() + 40 * this.absoluteYScale();
    }

    @EventInvoked
    public void keyPressed(KeyPressEvent e) {
      if (selected) {
        Keybind.this.setting.setValue(e.getKey());
        this.updateButton(e.getKey().getName());
        selected = false;
      }
    }

    private void updateButton(String string) {
      this.text.text(string);
      this.text.position(125 - this.text.width() / 2 * 4, 25 - this.text.height() / 2 * 4);
    }
  }
}

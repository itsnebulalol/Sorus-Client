package org.sorus.client.gui.theme.defaultTheme.selectComponent;

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
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;

import java.util.ArrayList;
import java.util.List;

public class SelectComponent extends Collection {

    public static final double WIDTH = 410, HEIGHT = 110;

    private final DefaultSelectComponentScreen theme;
    private final Component component;
    private final HollowRectangle border;

    private long prevClick;

    public SelectComponent(DefaultSelectComponentScreen theme, Component component) {
        this.theme = theme;
        this.component = component;
        final double ROUNDING = 10;
        this.add(
                new Rectangle()
                        .size(WIDTH, HEIGHT)
                        .smooth(ROUNDING)
                        .color(DefaultTheme.getForegroundColorNew()));
        this.add(
                border = new HollowRectangle()
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

    @EventInvoked
    public void onClick(MousePressEvent e) {
      if (e.getX() > this.absoluteX()
          && e.getX() < this.absoluteX() + WIDTH * this.absoluteXScale()
          && e.getY() > this.absoluteY()
          && e.getY() < this.absoluteY() + HEIGHT * this.absoluteYScale() && e.getY() * 1 / this.absoluteYScale() < 760) {
        this.select();
        if (System.currentTimeMillis() - prevClick < 500) {
          //receiver.call(this.component);
        }
      } else {
          this.deselect();
      }
      prevClick = System.currentTimeMillis();
    }

    public void select() {
        border.color(DefaultTheme.getElementSecondColorNew());
        if (theme.getSelected() != null) {
            theme.getSelected().deselect();
        }
        theme.setSelected(this);
    }

    public void deselect() {
        border.color(DefaultTheme.getElementMedgroundColorNew());
    }

    public Component getComponent() {
        return component;
    }
}
package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.HollowRectangle;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Scissor;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.IConfigurableTheme;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.DefaultThemeBase;
import org.sorus.client.settings.Setting;

public class DefaultTextBox extends DefaultThemeBase<TextBox> implements IConfigurableTheme {

  private final Collection main;

  private final Setting<String> setting;

  public DefaultTextBox(
      DefaultTheme theme, Collection main, Setting<String> setting, String description) {
    super(theme);
    this.main = main;
    this.setting = setting;
    main.add(new TextBoxInner().position(570, 10));
    main.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 30)
            .color(defaultTheme.getElementColorNew()));
  }

  @Override
  public void render() {
    this.main.onRender();
  }

  @Override
  public void exit() {
    this.main.onRemove();
  }

  @Override
  public double getHeight() {
    return 70;
  }

  public class TextBoxInner extends Collection {

    private final Rectangle rectangle;
    private final HollowRectangle hollowRectangle;
    private final Text text;

    private boolean selected;

    private String message;

    public TextBoxInner() {
      this.add(
          rectangle =
              new Rectangle()
                  .smooth(5)
                  .size(250, 50)
                  .color(defaultTheme.getElementBackgroundColorNew()));
      this.add(hollowRectangle = new HollowRectangle().thickness(2).smooth(5).size(250, 50));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer())
                  .scale(4, 4));
      this.message = setting.getValue();
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if (selected) {
        hollowRectangle.color(defaultTheme.getElementSecondColorNew());
        text.color(defaultTheme.getElementSecondColorNew());
      } else {
        hollowRectangle.color(defaultTheme.getElementForegroundColorNew());
        text.color(defaultTheme.getElementForegroundColorNew());
      }
      this.updateButton(message);
      this.rectangle.onRender();
      this.hollowRectangle.onRender();
      Scissor.addScissor(
          this.absoluteX() + 10 * this.absoluteXScale(),
          this.absoluteY(),
          240 * this.absoluteXScale(),
          50 * this.absoluteYScale());
      this.text.onRender();
      Scissor.removeScissor();
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
              && e.getY() < this.absoluteY() + 50 * this.absoluteYScale();
    }

    @EventInvoked
    public void keyPressed(KeyPressEvent e) {
      if (selected) {
        char character = e.getCharacter();
        switch (e.getKey()) {
          case SHIFT_LEFT:
          case SHIFT_RIGHT:
            return;
          case BACKSPACE:
            if (!message.isEmpty()) {
              this.message = message.substring(0, message.length() - 1);
            }
            break;
          default:
            this.message = message + character;
            break;
        }
        setting.setValue(this.message);
      }
    }

    private void updateButton(String string) {
      this.text.text(string + " ");
      if (this.text.width() > 220 * this.absoluteXScale()) {
        this.text.position(
            10 + 220 - (this.text.width() * 1 / this.absoluteXScale()),
            25 - this.text.height() / 2 * 4);
      } else {
        this.text.position(10, 25 - this.text.height() / 2 * 4);
      }
      if (selected) {
        this.text.text(string + (System.currentTimeMillis() % 1000 > 500 ? "_" : ""));
      }
    }
  }
}

package org.sorus.client.gui.theme.defaultTheme.settings.components;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.screen.settings.IConfigurableTheme;
import org.sorus.client.gui.screen.settings.components.Keybind;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.DefaultThemeBase;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Input;

public class DefaultKeybind extends DefaultThemeBase<Keybind> implements IConfigurableTheme {

  private final Collection main;
  private final Setting<Input> setting;

  public DefaultKeybind(
      DefaultTheme theme, Collection main, Setting<Input> setting, String description) {
    super(theme);
    this.main = main;
    this.setting = setting;
    main.add(new KeybindInner().position(575, 5));
    main.add(
        new Text()
            .fontRenderer(Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
            .text(description)
            .scale(3.5, 3.5)
            .position(30, 25)
            .color(defaultTheme.getElementColorNew()));
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
    main.onRender();
  }

  @Override
  public void exit() {
    main.onRemove();
  }

  @Override
  public double getHeight() {
    return 70;
  }

  public class KeybindInner extends Collection {

    private final Rectangle rectangle;
    private final Text text;

    private boolean selected;

    public KeybindInner() {
      this.add(rectangle = new Rectangle().smooth(5).size(250, 50));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getGidoleFontRenderer())
                  .scale(4, 4));
      this.updateButton(DefaultKeybind.this.setting.getValue().getName());
      Sorus.getSorus().getEventManager().register(this);
    }

    @Override
    public void onRender() {
      if (selected) {
        rectangle.color(defaultTheme.getElementMedgroundColorNew());
        text.color(defaultTheme.getElementColorNew());
        this.updateButton("?");
      } else {
        rectangle.color(defaultTheme.getElementBackgroundColorNew());
        text.color(defaultTheme.getElementSecondColorNew());
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
        DefaultKeybind.this.setting.setValue(e.getKey());
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

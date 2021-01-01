package org.sorus.client.module.impl.customscreens.mainmenu;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class MainMenu extends Screen {

  private final Panel main;
  private final Component image;

  public MainMenu() {
    main = new Panel(this);
    main.add(
        image = new Image().resource("sorus/modules/custommenus/background.png").size(1940, 1100));
    main.add(
        new Rectangle()
            .size(175, 175)
            .smooth(87.5)
            .color(DefaultTheme.getMedbackgroundLayerColor())
            .position(872.5, 302.5));
    main.add(
        new Image()
            .resource("sorus/logo.png")
            .size(150, 150)
            .color(DefaultTheme.getForegroundLayerColor())
            .position(885, 315));
    main.add(new MainMenuMainElement.Singleplayer().position(735, 500));
    main.add(new MainMenuMainElement.Multiplayer().position(735, 600));
    Collection bottomBar = new Collection();
    bottomBar.add(
        new Rectangle().size(300, 100).smooth(50).color(DefaultTheme.getMedbackgroundLayerColor()));
    bottomBar.position(810, 965);
    bottomBar.add(new MainMenuBottomElement.LanguagesButton().position(12.5, 12.5));
    bottomBar.add(new MainMenuBottomElement.SettingsButton().position(112.5, 12.5));
    bottomBar.add(new MainMenuBottomElement.ExitButton().position(212.5, 12.5));
    main.add(bottomBar);
  }

  @Override
  public void onRender() {
    double xPercent =
        Sorus.getSorus().getVersion().getData(IInput.class).getMouseX()
            / Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
    double yPercent =
        Sorus.getSorus().getVersion().getData(IInput.class).getMouseY()
            / Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
    image.position(-5 + (xPercent - 0.5) * 10, -5 + (yPercent - 0.5) * 10);
    this.main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    this.main.onRender();
  }

  @Override
  public void onKeyType(Key key, boolean repeat) {
    if (key == Key.F5) {
      Sorus.getSorus().getGUIManager().close(this);
      Sorus.getSorus().getGUIManager().open(new MainMenu());
    }
  }

  @Override
  public void onExit() {
    super.onExit();
    this.main.onRemove();
  }
}

package org.sorus.client.gui.theme.defaultTheme.mainmenu;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.theme.defaultTheme.DefaultTheme;
import org.sorus.client.gui.theme.defaultTheme.DefaultThemeBase;
import org.sorus.client.module.impl.customscreens.mainmenu.MainMenuScreen;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.input.IInput;
import org.sorus.client.version.input.Key;

public class DefaultMainMenuScreen extends DefaultThemeBase<MainMenuScreen> {

  public static final int SIDEBAR_COUNT = 6;

  private Panel main;
  private Component image;

  private int sidebarElementCount;

  public DefaultMainMenuScreen(DefaultTheme theme) {
    super(theme);
  }

  @Override
  public void init() {
    main = new Panel(this.getParent());
    main.add(
        image =
            new Image()
                .resource("sorus/modules/custommenus/mainmenu/background.png")
                .size(1940, 1100));
    main.add(
        new Rectangle()
            .size(300, 300)
            .smooth(150)
            .color(defaultTheme.getBackgroundColorNew())
            .position(810, 390));
    main.add(
        new Image()
            .resource("sorus/modules/custommenus/mainmenu/logo.png")
            .size(150, 150)
            .color(defaultTheme.getElementColorNew())
            .position(885, 465));
    this.addSidebarElement("sorus/modules/custommenus/mainmenu/logo.png", () -> {});
    this.addSidebarElement(
        "sorus/modules/custommenus/mainmenu/singleplayer.png",
        () -> {
          Sorus.getSorus().getGUIManager().close(this.getParent());
          Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.VIEW_WORLDS);
        });
    this.addSidebarElement(
        "sorus/modules/custommenus/mainmenu/multiplayer.png",
        () -> {
          Sorus.getSorus().getGUIManager().close(this.getParent());
          Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.VIEW_SERVERS);
        });
    this.addSidebarElement(
        "sorus/modules/custommenus/mainmenu/settings.png",
        () -> {
          Sorus.getSorus().getGUIManager().close(this.getParent());
          Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.SETTINGS);
        });
    this.addSidebarElement(
        "sorus/modules/custommenus/mainmenu/languages.png",
        () -> {
          Sorus.getSorus().getGUIManager().close(this.getParent());
          Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.LANGUAGES);
        });
    this.addSidebarElement(
        "sorus/modules/custommenus/mainmenu/exit.png",
        () -> {
          Sorus.getSorus().getVersion().getData(IGame.class).shutdown();
        });
  }

  private void addSidebarElement(String imagePath, Runnable runnable) {
    main.add(
        new SideBarElement(this, imagePath, runnable)
            .position(0, 1080.0 / DefaultMainMenuScreen.SIDEBAR_COUNT * sidebarElementCount));
    sidebarElementCount++;
  }

  @Override
  public void update() {
    main.onUpdate();
  }

  @Override
  public void render() {
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
      Sorus.getSorus().getGUIManager().close(this.getParent());
      Sorus.getSorus().getGUIManager().open(new MainMenuScreen());
    }
  }

  @Override
  public void exit() {
    this.main.onRemove();
  }
}

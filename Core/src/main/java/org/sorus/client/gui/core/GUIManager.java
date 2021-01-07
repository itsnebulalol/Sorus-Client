package org.sorus.client.gui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.TickEvent;
import org.sorus.client.event.impl.client.input.KeyPressEvent;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.event.impl.client.input.MouseReleaseEvent;
import org.sorus.client.event.impl.client.render.Render2DEvent;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;

public class GUIManager {

  private final List<Screen> currentScreens = new ArrayList<>();

  private RendererImpl renderer;

  private boolean isBlankOpen = false;

  public void initialize() {
    Sorus.getSorus().getEventManager().register(this);
    this.renderer = new RendererImpl();
  }

  public void open(Screen screen) {
    if (screen == null) {
      return;
    }
    this.currentScreens.add(screen);
    screen.onOpen();
  }

  public void close(Screen screen) {
    this.currentScreens.remove(screen);
    screen.onExit();
  }

  @EventInvoked
  public void onRender(Render2DEvent e) {
    for (Screen screen : currentScreens) {
      screen.onRender();
    }
  }

  @EventInvoked
  public void onTick(TickEvent e) {
    boolean shouldBlankOpen = false;
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    for (Screen screen : currentScreens) {
      screen.onUpdate();
      if (screen.shouldOpenBlank()) {
        shouldBlankOpen = true;
      }
    }
    this.updateBlankGUI(shouldBlankOpen);
  }

  private void updateBlankGUI(boolean shouldBlankOpen) {
    if (shouldBlankOpen != isBlankOpen) {
      IGame game = Sorus.getSorus().getVersion().getData(IGame.class);
      if (shouldBlankOpen) {
        game.display(GUIType.BLANK);
      } else {
        game.removeBlankGUI();
      }
      isBlankOpen = shouldBlankOpen;
    }
  }

  @EventInvoked
  public void onKeyPress(KeyPressEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    for (Screen screen : currentScreens) {
      screen.onKeyType(e.getKey(), e.isRepeat());
    }
  }

  @EventInvoked
  public void onMousePress(MousePressEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    for (Screen screen : currentScreens) {
      screen.onMouseClick(e.getButton(), e.getX(), e.getY());
    }
  }

  @EventInvoked
  public void onMouseRelease(MouseReleaseEvent e) {
    List<Screen> currentScreens = new ArrayList<>(this.currentScreens);
    for (Screen screen : currentScreens) {
      screen.onMouseRelease(e.getButton());
    }
  }

  public RendererImpl getRenderer() {
    return renderer;
  }

  public List<Screen> getCurrentScreens() {
    return currentScreens;
  }

  public <T extends Screen> T getCurrentScreen(Class<T> clazz) {
    Stream<T> stream = (Stream<T>) this.currentScreens.stream();
    Optional<T> screenOptional =
        stream.filter(screen -> clazz.isAssignableFrom(screen.getClass())).findFirst();
    return screenOptional.orElse(null);
  }

  public boolean isScreenOpen(Class<? extends Screen> clazz) {
    return this.getCurrentScreen(clazz) != null;
  }
}

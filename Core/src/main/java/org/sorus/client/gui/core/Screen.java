package org.sorus.client.gui.core;

import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.api.IInputReceiver;
import org.sorus.client.gui.core.component.api.IRenderable;
import org.sorus.client.gui.core.component.api.IScreenRenderer;

public abstract class Screen implements IInputReceiver, IRenderable, IContainer {

  protected final IScreenRenderer renderer;

  public Screen() {
    this.renderer = Sorus.getSorus().getGUIManager().getRenderer();
  }

  public void onOpen() {}

  public void onUpdate() {}

  public void onExit() {}

  public boolean shouldOpenBlank() {
    return false;
  }

  @Override
  public boolean isTopLevel() {
    List<Screen> currentScreens = Sorus.getSorus().getGUIManager().getCurrentScreens();
    return currentScreens.indexOf(this) == currentScreens.size() - 1;
  }
}

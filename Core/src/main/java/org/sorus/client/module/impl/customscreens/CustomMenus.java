package org.sorus.client.module.impl.customscreens;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.module.Module;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.module.impl.customscreens.mainmenu.MainMenu;
import org.sorus.client.version.game.GUIType;
import org.sorus.client.version.game.IGame;

public class CustomMenus extends Module {

  public CustomMenus() {
    super("Custom Menus");
  }

  @Override
  public void onLoad() {
    Sorus.getSorus().getEventManager().register(this);
    super.onLoad();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @EventInvoked
  public void override(GuiSwitchEvent e) {
    if (e.getType().equals(GUIType.MAIN_MENU)) {
      Sorus.getSorus().getVersion().getData(IGame.class).display(GUIType.BLANK);
      Sorus.getSorus().getGUIManager().open(new MainMenu());
    } else {
      if (Sorus.getSorus().getGUIManager().isScreenOpen(MainMenu.class)) {
        Sorus.getSorus()
            .getGUIManager()
            .close(Sorus.getSorus().getGUIManager().getCurrentScreen(MainMenu.class));
      }
    }
  }
}

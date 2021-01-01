package org.sorus.client.gui.theme.defaultTheme;

import org.sorus.client.Sorus;
import org.sorus.client.gui.hud.HUD;
import org.sorus.client.gui.hud.HUDManager;
import org.sorus.client.gui.hud.HUDRenderScreen;
import org.sorus.client.gui.theme.ThemeBase;
import org.sorus.client.version.game.IGame;

public class DefaultHUDRenderScreen extends ThemeBase<HUDRenderScreen> {

  private final HUDManager hudManager;

  public DefaultHUDRenderScreen() {
    this.hudManager = Sorus.getSorus().getHUDManager();
  }

  @Override
  public void render() {
    if (this.shouldRenderHUDS()) {
      for (HUD hud : this.hudManager.getHUDs()) {
        hud.render();
      }
    }
  }

  private boolean shouldRenderHUDS() {
    Sorus sorus = Sorus.getSorus();
    return sorus.getVersion().getData(IGame.class).shouldRenderHUDS()
        && sorus.getVersion().getData(IGame.class).getPlayer().exists();
  }
}

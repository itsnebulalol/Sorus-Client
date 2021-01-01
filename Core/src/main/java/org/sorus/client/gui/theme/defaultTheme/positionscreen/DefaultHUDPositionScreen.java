package org.sorus.client.gui.theme.defaultTheme.positionscreen;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.hud.positonscreen.HUDPositionScreen;
import org.sorus.client.gui.theme.defaultTheme.DefaultSomethingScreen;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.render.IRenderer;

public class DefaultHUDPositionScreen extends DefaultSomethingScreen<HUDPositionScreen> {

  private final Panel main = new Panel();

  private long initTime;

  private final boolean fadeIn;

  public DefaultHUDPositionScreen(boolean animateIn) {
    super(animateIn, 0);
    this.fadeIn = animateIn;
  }

  @Override
  public void init() {
    if (!this.fadeIn) {
      Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
    }
    initTime = System.currentTimeMillis();
    super.init();
  }

  @Override
  public void render() {
    main.scale(
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth() / 1920,
        Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight() / 1080);
    double fadeInPercent = Math.min((System.currentTimeMillis() - initTime) / 150.0, 1);
    this.main.color(new Color(255, 255, 255, (int) (fadeInPercent * 255))).onRender();
    super.render();
    if (this.fadeIn) {
      Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
      Sorus.getSorus()
          .getVersion()
          .getData(IRenderer.class)
          .enableBlur(Math.round(fadeInPercent * 7.5 * 2.0) / 2.0);
    }
  }

  @Override
  public void exit() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
    Sorus.getSorus().getSettingsManager().save();
    this.main.onRemove();
    super.exit();
  }
}

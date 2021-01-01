package org.sorus.client.module.impl.crosshair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.IScreen;

public class Crosshair extends ModuleConfigurable {

  private final List<CrosshairMode> registeredModes = new ArrayList<>();
  private final List<String> registeredModeNames = new ArrayList<>();
  private CrosshairMode currentMode;

  private final Setting<Long> mode;

  public Crosshair() {
    super("Crosshair");
    this.register(new CrosshairMode.SplitLinesMode());
    this.register(
        mode =
            new Setting<Long>("mode", 0L) {
              @Override
              public void setValue(Long value) {
                Crosshair.this.setMode(registeredModes.get(value.intValue()));
                super.setValue(value);
              }
            });
    Sorus.getSorus().getEventManager().register(this);
    this.currentMode = this.registeredModes.get(0);
  }

  public void register(CrosshairMode mode) {
    this.registeredModes.add(mode);
    this.registeredModeNames.add(mode.getName());
  }

  public void setMode(CrosshairMode mode) {
    if (this.currentMode != null) {
      for (Setting<?> setting : this.currentMode.getSettings()) {
        this.unregister(setting);
      }
    }
    this.currentMode = mode;
    for (Setting<?> setting : this.currentMode.getSettings()) {
      this.register(setting);
    }
  }

  @EventInvoked
  public void onRenderCrosshair(RenderObjectEvent.Crosshair e) {
    if (this.shouldHideCrosshair()) {
      e.setCancelled(true);
      double scaledWidth = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth();
      double scaledHeight = Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight();
      this.renderCrosshair(scaledWidth / 2, scaledHeight / 2);
      Sorus.getSorus().getVersion().getData(IGLHelper.class).blend(true);
    }
  }

  public boolean shouldHideCrosshair() {
    return this.isEnabled();
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, registeredModeNames, "Mode"));
    this.currentMode.addConfigComponents(collection);
    collection.add(new ViewCrosshair());
  }

  public void renderCrosshair(double xCenter, double yCenter) {
    this.currentMode.render(xCenter, yCenter);
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Customize your crosshair without needing to mess with a resource pack!";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(
        new Image()
            .resource("sorus/modules/crosshair/logo.png")
            .size(80, 80)
            .color(new Color(175, 175, 175)));
  }
}

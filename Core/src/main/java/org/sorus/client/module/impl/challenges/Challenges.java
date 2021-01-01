package org.sorus.client.module.impl.challenges;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class Challenges extends ModuleConfigurable {

  private final Setting<Boolean> noCrosshair;

  public Challenges() {
    super("Challenges");
    this.register(noCrosshair = new Setting<>("noCrosshair", false));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(noCrosshair, "No Crosshair"));
  }

  @EventInvoked
  public void onRenderCrosshair(RenderObjectEvent.Crosshair e) {
    if (this.shouldHideCrosshair()) {
      e.setCancelled(true);
    }
  }

  public boolean shouldHideCrosshair() {
    return this.isEnabled() && this.noCrosshair.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Minecraft too easy? Some challenges to make it a little harder.";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/challenges/logo.png").size(80, 80));
  }
}

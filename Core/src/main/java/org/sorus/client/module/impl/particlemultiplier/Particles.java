package org.sorus.client.module.impl.particlemultiplier;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class Particles extends ModuleConfigurable {

  private final Setting<Double> multiplier;
  private final Setting<Boolean> alwaysParticles;
  private final Setting<Boolean> alwaysSharp;

  public Particles() {
    super("Particles");
    this.register(multiplier = new Setting<>("multiplier", 1.0));
    this.register(alwaysParticles = new Setting<>("alwaysParticles", false));
    this.register(alwaysSharp = new Setting<>("alwaysSharp", false));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Slider(multiplier, 0, 5, "Multiplier"));
    collection.add(new Toggle(alwaysSharp, "Always Show Sharp Particles"));
    collection.add(new Toggle(alwaysParticles, "Always Show Regular Particles"));
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  public double getMultiplier() {
    return this.isEnabled() ? multiplier.getValue() : 1;
  }

  public boolean isAlwaysSharp() {
    return this.isEnabled() && this.alwaysSharp.getValue();
  }

  public boolean isAlwaysParticles() {
    return this.isEnabled() && this.alwaysParticles.getValue();
  }
}

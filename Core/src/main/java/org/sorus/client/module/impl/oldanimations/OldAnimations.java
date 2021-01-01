package org.sorus.client.module.impl.oldanimations;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class OldAnimations extends ModuleConfigurable {

  private final Setting<Boolean> animInteractBreak;
  private final Setting<Boolean> redArmorFlashAttack;

  public OldAnimations() {
    super("Old Animations");
    this.register(animInteractBreak = new Setting<>("animInteractBreak", false));
    this.register(redArmorFlashAttack = new Setting<>("redArmorFlashAttack", false));
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/oldanimations/logo.png").size(80, 80));
  }

  @Override
  public String getDescription() {
    return "Various animation changes to make the 1.8.9 game feel like 1.7.10.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(animInteractBreak, "Interact and Break Animation").position(30, 30));
    collection.add(new Toggle(redArmorFlashAttack, "Red Armor on Damage").position(30, 100));
  }

  public boolean shouldShowAnimInteractBreak() {
    return this.isEnabled() && animInteractBreak.getValue();
  }

  public boolean shouldShowRedArmor() {
    return this.isEnabled() && redArmorFlashAttack.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.AllowCertain("1.8.9");
  }
}

package org.sorus.client.module.impl.names;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.TextBox;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class Names extends ModuleConfigurable {

  private final Setting<Boolean> selfNameTag;
  private final Setting<Boolean> customName;
  private final Setting<String> customNameText;
  private final Setting<Boolean> overrideNameTag;

  public Names() {
    super("Custom Name");
    this.register(selfNameTag = new Setting<>("selfNameTag", false));
    this.register(customName = new Setting<>("customName", false));
    this.register(customNameText = new Setting<>("customNameText", ""));
    this.register(overrideNameTag = new Setting<>("overrideNameTag", false));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(selfNameTag, "Show Own Name Tag"));
    collection.add(new Toggle(customName, "Custom Name"));
    collection.add(new TextBox(customNameText, "Custom Name"));
    collection.add(new Toggle(overrideNameTag, "Override Name Tag"));
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  public boolean renderSelfName() {
    return this.isEnabled() && selfNameTag.getValue();
  }

  public boolean customName() {
    return this.isEnabled() && customName.getValue();
  }

  public boolean overrideNameTag() {
    return this.overrideNameTag.getValue();
  }

  public String getCustomName() {
    return this.customNameText.getValue();
  }

  @Override
  public String getDescription() {
    return "Customize your name (locally) to be whoever you want!";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/names/logo.png").size(80, 80));
  }
}

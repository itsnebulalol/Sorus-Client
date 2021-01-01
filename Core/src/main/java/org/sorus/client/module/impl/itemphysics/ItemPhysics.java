package org.sorus.client.module.impl.itemphysics;

import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;

public class ItemPhysics extends ModuleConfigurable {

  public ItemPhysics() {
    super("Item Physics");
  }

  @Override
  public String getDescription() {
    return "Makes dropped items look more realistic.";
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

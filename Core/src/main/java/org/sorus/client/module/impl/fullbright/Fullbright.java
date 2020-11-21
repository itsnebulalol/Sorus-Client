package org.sorus.client.module.impl.fullbright;

import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;

public class Fullbright extends ModuleConfigurable {

  public Fullbright() {
    super("FULLBRIGHT");
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

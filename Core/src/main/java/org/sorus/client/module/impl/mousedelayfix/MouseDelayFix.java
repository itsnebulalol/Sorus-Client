package org.sorus.client.module.impl.mousedelayfix;

import org.sorus.client.module.Module;
import org.sorus.client.module.VersionDecision;

public class MouseDelayFix extends Module {

  public MouseDelayFix() {
    super("Mouse Delay Fix");
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.DenyCertain("1.7.10");
  }
}

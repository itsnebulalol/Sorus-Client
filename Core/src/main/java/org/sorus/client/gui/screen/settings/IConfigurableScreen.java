package org.sorus.client.gui.screen.settings;

import org.sorus.client.gui.core.component.Collection;

public interface IConfigurableScreen {

  void addConfigComponents(Collection collection);

  String getDisplayName();
}

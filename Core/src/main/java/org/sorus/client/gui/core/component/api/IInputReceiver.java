package org.sorus.client.gui.core.component.api;

import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

public interface IInputReceiver {

  default void onMouseClick(Button button, double x, double y) {}

  default void onMouseRelease(Button button) {}

  default void onKeyType(Key key, boolean repeat) {}
}

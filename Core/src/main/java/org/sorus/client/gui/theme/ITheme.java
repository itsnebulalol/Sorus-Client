package org.sorus.client.gui.theme;

import org.sorus.client.version.input.Button;
import org.sorus.client.version.input.Key;

public interface ITheme<T> {

  default void init() {}

  default void update() {}

  default void render() {}

  default void exit() {}

  default void onKeyType(Key key, boolean repeat) {}

  default void onMouseClick(Button button, double x, double y) {}

  default void onMouseRelease(Button button) {}

  default void setParent(T parent) {}
}

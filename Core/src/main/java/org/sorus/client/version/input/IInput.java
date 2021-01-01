package org.sorus.client.version.input;

public interface IInput {

  double getMouseX();

  double getMouseY();

  boolean isButtonDown(Button button);

  boolean isKeyDown(Key key);

  double getScroll();

  IKeybind getKeybind(KeybindType keybind);
}

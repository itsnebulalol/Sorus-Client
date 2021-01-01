package org.sorus.client.gui.screen;

public interface Callback<T> {

  void call(T selected);

  void cancel();
}

package org.sorus.client.plugin;

public interface IPlugin {

  default void onLoad() {}

  default void onUnload() {}
}

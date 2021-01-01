package org.sorus.client.module;

public abstract class Module {

  private final String name;

  public Module(String name) {
    this.name = name;
  }

  public void onLoad() {}

  public String getName() {
    return name;
  }

  public abstract VersionDecision getVersions();
}

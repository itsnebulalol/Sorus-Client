package org.sorus.client.module;

import java.util.Arrays;

public abstract class VersionDecision {

  public abstract boolean allow(String version);

  public static class AllowCertain extends VersionDecision {

    private final String[] versions;

    public AllowCertain(String... versions) {
      this.versions = versions;
    }

    @Override
    public boolean allow(String version) {
      return Arrays.asList(versions).contains(version);
    }
  }

  public static class DenyCertain extends VersionDecision {

    private final String[] versions;

    public DenyCertain(String... versions) {
      this.versions = versions;
    }

    @Override
    public boolean allow(String version) {
      return !Arrays.asList(versions).contains(version);
    }
  }

  public static class Allow extends VersionDecision {

    @Override
    public boolean allow(String version) {
      return true;
    }
  }
}

package org.sorus.client.module.impl.music;

public interface ISound {

  void play(double percent);

  void stop();

  boolean playing();

  double getPlayPercent();

  void addCompletionHook(ICompletionHook hook);

  String getName();

  boolean isComplete();
}

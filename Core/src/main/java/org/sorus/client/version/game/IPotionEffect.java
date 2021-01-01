package org.sorus.client.version.game;

public interface IPotionEffect {

  String getName();

  String getAmplifier();

  String getDuration();

  void drawIcon(double x, double y, double width, double height);
}

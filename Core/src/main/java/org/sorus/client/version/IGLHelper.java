package org.sorus.client.version;

import org.sorus.client.util.Axis;

public interface IGLHelper {

  void scale(double x, double y, double z);

  void translate(double x, double y, double z);

  void rotate(Axis axis, double value);

  void blend(boolean blend);

  void color(double red, double green, double blue, double alpha);

  void lineWidth(double lineWidth);

  void beginScissor(double x, double y, double width, double height);

  void endScissor();

  void unbindTexture();

  void bind(String resource);

  void push();

  void pop();
}

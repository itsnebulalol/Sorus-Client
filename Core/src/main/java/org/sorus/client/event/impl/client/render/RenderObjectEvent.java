package org.sorus.client.event.impl.client.render;

import org.sorus.client.event.EventCancelable;

public class RenderObjectEvent extends EventCancelable {

  public static class Crosshair extends RenderObjectEvent {}

  public static class BlockOverlay extends RenderObjectEvent {}

  public static class Text extends RenderObjectEvent {

    private String text;

    public Text(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

  public static class GradientRectangle extends RenderObjectEvent {

    private final int left, right;
    private final int top, bottom;
    private final int color1, color2;

    public GradientRectangle(int left, int top, int right, int bottom, int color1, int color2) {
      this.left = left;
      this.top = top;
      this.right = right;
      this.bottom = bottom;
      this.color1 = color1;
      this.color2 = color2;
    }

    public int getLeft() {
      return left;
    }

    public int getRight() {
      return right;
    }

    public int getTop() {
      return top;
    }

    public int getBottom() {
      return bottom;
    }

    public int getColor1() {
      return color1;
    }

    public int getColor2() {
      return color2;
    }
  }

  public static class Sidebar extends RenderObjectEvent {}
}

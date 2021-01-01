package org.sorus.client.cosmetic;

import org.sorus.client.Sorus;
import org.sorus.client.version.render.IRenderer;

public class TexturedQuad {

  private final Vertex[] vertices;
  private final TexturePosition[] texturePositions;

  public TexturedQuad(Vertex[] vertices, TexturePosition[] texturePositions) {
    this.vertices = vertices;
    this.texturePositions = texturePositions;
  }

  public void render() {
    Sorus.getSorus().getVersion().getData(IRenderer.class).drawQuad(this);
  }

  public Vertex[] getVertices() {
    return vertices;
  }

  public TexturePosition[] getTexturePositions() {
    return texturePositions;
  }

  public static class Vertex {

    private final double x, y, z;

    public Vertex(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }

    public double getZ() {
      return z;
    }
  }

  public static class TexturePosition {

    private double textureX, textureY;

    public TexturePosition(double textureX, double textureY) {
      this.setTexturePosition(textureX, textureY);
    }

    public void setTexturePosition(double textureX, double textureY) {
      this.textureX = textureX;
      this.textureY = textureY;
    }

    public double getTextureX() {
      return textureX;
    }

    public double getTextureY() {
      return textureY;
    }
  }
}

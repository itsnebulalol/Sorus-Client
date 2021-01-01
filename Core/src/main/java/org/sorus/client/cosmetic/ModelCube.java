package org.sorus.client.cosmetic;

public class ModelCube {

  private final TexturedQuad[] quads;

  public ModelCube(
      ModelRenderer renderer,
      int textureX,
      int textureY,
      float x,
      float y,
      float z,
      float width,
      float height,
      float depth,
      double scale) {
    this.quads = new TexturedQuad[6];
    float textureWidth = renderer.getModel().getTextureWidth();
    float textureHeight = renderer.getModel().getTextureHeight();
    float scaledTextureX = (float) textureX / textureWidth;
    float scaledTextureY = (float) textureY / textureHeight;
    TexturedQuad.Vertex vertex0 = new TexturedQuad.Vertex(x, y, z);
    TexturedQuad.Vertex vertex1 = new TexturedQuad.Vertex(x, y, z + depth * scale);
    TexturedQuad.Vertex vertex2 = new TexturedQuad.Vertex(x, y + height * scale, z + depth * scale);
    TexturedQuad.Vertex vertex3 = new TexturedQuad.Vertex(x, y + height * scale, z);
    TexturedQuad.Vertex vertex4 = new TexturedQuad.Vertex(x + width * scale, y, z + depth * scale);
    TexturedQuad.Vertex vertex5 =
        new TexturedQuad.Vertex(x + width * scale, y + height * scale, z + depth * scale);
    TexturedQuad.Vertex vertex6 = new TexturedQuad.Vertex(x + width * scale, y, z);
    TexturedQuad.Vertex vertex7 = new TexturedQuad.Vertex(x + width * scale, y + height * scale, z);
    TexturedQuad.TexturePosition pos0 =
        new TexturedQuad.TexturePosition(
            scaledTextureX, scaledTextureY + width / textureHeight * 2);
    TexturedQuad.TexturePosition pos1 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth, scaledTextureY + width / textureHeight * 2);
    TexturedQuad.TexturePosition pos2 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth, scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos3 =
        new TexturedQuad.TexturePosition(scaledTextureX, scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos4 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth + depth / textureWidth,
            scaledTextureY + width / textureHeight * 2);
    TexturedQuad.TexturePosition pos5 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth + depth / textureWidth,
            scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos6 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth,
            scaledTextureY + width / textureHeight * 2);
    TexturedQuad.TexturePosition pos7 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth,
            scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos8 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth * 2,
            scaledTextureY + width / textureHeight * 2);
    TexturedQuad.TexturePosition pos9 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth * 2,
            scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos10 =
        new TexturedQuad.TexturePosition(scaledTextureX + width / textureWidth, scaledTextureY);
    TexturedQuad.TexturePosition pos11 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth + depth / textureWidth, scaledTextureY);
    TexturedQuad.TexturePosition pos12 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth,
            scaledTextureY + width / textureHeight);
    TexturedQuad.TexturePosition pos13 =
        new TexturedQuad.TexturePosition(
            scaledTextureX + width / textureWidth * 2 + depth / textureWidth, scaledTextureY);
    this.quads[0] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex0, vertex1, vertex2, vertex3},
            new TexturedQuad.TexturePosition[] {pos0, pos1, pos2, pos3});
    this.quads[1] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex1, vertex4, vertex5, vertex2},
            new TexturedQuad.TexturePosition[] {pos1, pos4, pos5, pos2});
    this.quads[2] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex4, vertex6, vertex7, vertex5},
            new TexturedQuad.TexturePosition[] {pos4, pos6, pos7, pos5});
    this.quads[3] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex6, vertex0, vertex3, vertex7},
            new TexturedQuad.TexturePosition[] {pos6, pos8, pos9, pos7});
    this.quads[4] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex2, vertex5, vertex7, vertex3},
            new TexturedQuad.TexturePosition[] {pos2, pos5, pos11, pos10});
    this.quads[5] =
        new TexturedQuad(
            new TexturedQuad.Vertex[] {vertex4, vertex1, vertex0, vertex6},
            new TexturedQuad.TexturePosition[] {pos5, pos12, pos13, pos11});
  }

  public void render() {
    for (TexturedQuad quad : quads) {
      if (quad != null) {
        quad.render();
      }
    }
  }
}

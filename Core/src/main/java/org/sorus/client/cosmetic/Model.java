package org.sorus.client.cosmetic;

import org.sorus.client.version.game.IPlayer;

public abstract class Model {

  private final String texture;
  private final int textureWidth, textureHeight;

  public Model(String texture, int textureWidth, int textureHeight) {
    this.texture = texture;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
  }

  public abstract void render(IPlayer player);

  public String getTexture() {
    return texture;
  }

  public int getTextureWidth() {
    return textureWidth;
  }

  public int getTextureHeight() {
    return textureHeight;
  }
}

package org.sorus.client.cosmetic.impl;

import org.sorus.client.cosmetic.Cosmetic;
import org.sorus.client.cosmetic.Model;
import org.sorus.client.cosmetic.ModelRenderer;
import org.sorus.client.version.game.IPlayer;

public class Wings extends Cosmetic {

  public Wings() {
    super("Wings", new WingsModel());
  }

  private static class WingsModel extends Model {

    private final ModelRenderer wing;

    public WingsModel() {
      super("sorus/cosmetics/wings/wings.png", 32, 88);
      wing = new ModelRenderer(this);
      wing.addCube(0, 0, -0.2f, 1, -0.2f, 8, 80, 8, 1 / 128.);
      wing.addRotationPoint("playerRotation", 0, 0, 0);
    }

    @Override
    public void render(IPlayer player) {
      wing.getRotationPoint("playerRotation").updateRotation(0, -player.getBodyRotation(), 0);
      wing.render();
    }
  }
}

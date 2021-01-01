package org.sorus.client.cosmetic;

import org.sorus.client.Sorus;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.game.IPlayer;

public abstract class Cosmetic {

  private final String name;
  private final String id;
  private final Model model;

  public Cosmetic(String name, Model model) {
    this.name = name;
    this.id = name.toLowerCase().replace(" ", "-");
    this.model = model;
  }

  public void render(IPlayer player) {
    Sorus.getSorus().getVersion().getData(IGLHelper.class).bind(this.model.getTexture());
    this.model.render(player);
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
}

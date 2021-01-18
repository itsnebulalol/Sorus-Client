package org.sorus.client.cosmetic;

import java.util.ArrayList;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.cosmetic.impl.Wings;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderEntityEvent;

public class CosmeticManager {

  private final List<Cosmetic> cosmetics = new ArrayList<>();

  public void onLoad() {
    Sorus.getSorus().getEventManager().register(this);
    this.registerInternalCosmetics();
  }

  public void register(Cosmetic cosmetic) {
    this.cosmetics.add(cosmetic);
  }

  public void registerInternalCosmetics() {
    this.register(new Wings());
  }

  @EventInvoked
  public void onRenderPlayer(RenderEntityEvent e) {
    /*if (e.getEntity().getType() == EntityType.PLAYER) {
      for (Cosmetic cosmetic : this.cosmetics) {
        cosmetic.render((IPlayer) e.getEntity());
      }
    }*/
  }
}

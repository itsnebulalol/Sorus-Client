package org.sorus.client.version.game;

import java.util.List;

public interface ILivingEntity extends IEntity {

  List<IPotionEffect> getActivePotionEffects();

  double getBodyRotation();
}

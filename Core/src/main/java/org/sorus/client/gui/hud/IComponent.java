package org.sorus.client.gui.hud;

import java.util.Map;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.settings.ISettingHolder;

public interface IComponent extends ISettingHolder {

  default void update(boolean dummy) {}

  void render(double x, double y, boolean dummy);

  default void onRemove() {}

  double getWidth();

  double getHeight();

  String getName();

  void setHUD(HUD hud);

  default void onAdd() {}

  @Override
  Map<String, Object> getSettings();

  default void addIconElements(Collection collection) {}
}

package org.sorus.client.version.game;

import java.util.List;

public interface IGame {

  boolean isIngame();

  boolean shouldRenderHUDS();

  void display(GUIType type);

  void removeBlankGUI();

  String getCurrentServerIP();

  int getFPS();

  IPlayer getPlayer();

  PerspectiveMode getPerspective();

  void setPerspective(PerspectiveMode perspective);

  void sendChatMessage(String message);

  int getPing();

  IItemManager getItemManager();

  void shutdown();

  IScoreboard getScoreboard();

  List<IPotionEffect> getDummyEffects();

  List<IItemStack> getDummyArmor();

  void setSmoothCamera(boolean smoothCamera);
}

package org.sorus.client.gui.hud;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.screen.Callback;
import org.sorus.client.gui.screen.SelectComponentScreen;
import org.sorus.client.gui.screen.hudlist.HUDListScreen;
import org.sorus.client.gui.screen.settings.IConfigurableScreen;
import org.sorus.client.gui.screen.settings.SettingsScreen;

public class SingleHUD extends HUD {

  @Override
  public void addComponent(IComponent component) {
    this.getComponents().clear();
    this.getComponents().add(component);
    component.setHUD(this);
    component.onAdd();
  }

  @Override
  public void onCreation() {
    Sorus.getSorus()
        .getGUIManager()
        .open(
            new SelectComponentScreen(new HUDListScreen(), new OnSelectScreenExit()));
  }

  @Override
  public void displaySettings(Screen currentScreen) {
    Sorus.getSorus()
        .getGUIManager()
        .open(new SettingsScreen(currentScreen, (IConfigurableScreen) this.getComponents().get(0)));
  }

  @Override
  public String getDescription() {
    return ((Component) this.getComponents().get(0)).getDescription();
  }

  public class OnSelectScreenExit implements Callback<IComponent> {

    @Override
    public void call(IComponent selected) {
      SingleHUD.this.addComponent(selected);
      SingleHUD.this.setName(selected.getName());
      SingleHUD.this.displaySettings(new HUDListScreen());
      Sorus.getSorus().getHUDManager().register(SingleHUD.this);
    }

    @Override
    public void cancel() {
      Sorus.getSorus().getHUDManager().unregister(SingleHUD.this);
    }
  }
}

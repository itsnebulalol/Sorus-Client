package org.sorus.client.gui.hud;

import java.util.*;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.Screen;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.settings.ISettingHolder;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.IGLHelper;
import org.sorus.client.version.IScreen;
import org.sorus.client.version.game.IGame;

public class HUD implements ISettingHolder {

  private final Setting<String> name;

  private final List<Setting<?>> settings = new ArrayList<>();

  private final Setting<HUDPosition> position;

  private final Setting<Double> scale;

  private final Setting<List<IComponent>> components;

  public HUD() {
    this.register(name = new Setting<>("name", null));
    this.register(position = new Setting<>("position", new HUDPosition()));
    this.register(scale = new Setting<>("scale", 1.0));
    components = new Setting<>("components", new ArrayList<>());
  }

  private void register(Setting<?> setting) {
    this.settings.add(setting);
  }

  public void addComponent(IComponent component) {
    this.components.getValue().add(component);
    component.setHUD(this);
    component.onAdd();
  }

  public void removeComponent(Component component) {
    this.components.getValue().remove(component);
    component.onRemove();
  }

  public void render() {
    List<IComponent> components = this.components.getValue();
    boolean dummy =
        !Sorus.getSorus().getVersion().getData(IGame.class).shouldRenderHUDS()
            && !Sorus.getSorus().getVersion().getData(IGame.class).isIngame();
    for (IComponent component : components) {
      component.update(dummy);
    }
    double x = this.getLeft();
    double y = this.getTop();
    IGLHelper glHelper = Sorus.getSorus().getVersion().getData(IGLHelper.class);
    glHelper.translate(x, y, 0);
    double scale = this.scale.getValue();
    glHelper.scale(scale, scale, 1);
    glHelper.translate(-x, -y, 0);
    double yOffset = 0;
    for (IComponent component : this.components.getValue()) {
      component.render(x, y + yOffset, dummy);
      yOffset += component.getHeight();
    }
    glHelper.translate(x, y, 0);
    glHelper.scale(1 / scale, 1 / scale, 1);
    glHelper.translate(-x, -y, 0);
  }

  public double getX() {
    return this.position
        .getValue()
        .getX(this, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth());
  }

  public void setX(double x) {
    this.position
        .getValue()
        .setX(this, x, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledWidth());
  }

  public double getY() {
    return this.position
        .getValue()
        .getY(this, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight());
  }

  public void setY(double y) {
    this.position
        .getValue()
        .setY(this, y, Sorus.getSorus().getVersion().getData(IScreen.class).getScaledHeight());
  }

  public double getScale() {
    return scale.getValue();
  }

  public void setScale(double scale) {
    this.scale.setValue(scale);
  }

  public double getWidth() {
    double width = 0;
    for (IComponent component : components.getValue()) {
      width = Math.max(width, component.getWidth());
    }
    return width;
  }

  public double getScaledWidth() {
    return this.getWidth() * this.getScale();
  }

  public double getHeight() {
    double height = 0;
    for (IComponent component : components.getValue()) {
      height = height + component.getHeight();
    }
    return height;
  }

  public double getScaledHeight() {
    return this.getHeight() * this.getScale();
  }

  public double getLeft() {
    return this.getX() - this.getScaledWidth() / 2;
  }

  public double getRight() {
    return this.getX() + this.getScaledWidth() / 2;
  }

  public double getTop() {
    return this.getY() - this.getScaledHeight() / 2;
  }

  public double getBottom() {
    return this.getY() + this.getScaledHeight() / 2;
  }

  public String getName() {
    return name.getValue();
  }

  public void setName(String name) {
    this.name.setValue(name);
  }

  public void onCreation() {
    this.setName("HUD");
    Sorus.getSorus().getGUIManager().open(new HUDConfigScreen(this));
    Sorus.getSorus().getHUDManager().register(this);
  }

  public String getDescription() {
    return "";
  }

  public void displaySettings(Screen currentScreen) {
    Sorus.getSorus().getGUIManager().open(new HUDConfigScreen(this));
  }

  public List<IComponent> getComponents() {
    return components.getValue();
  }

  @Override
  public Object getSettings() {
    Map<String, Object> hudSettings = new HashMap<>();
    List<Pair<Class<?>, Object>> componentSettings = new ArrayList<>();
    for (IComponent component : this.components.getValue()) {
      componentSettings.add(Pair.of(component.getClass(), component.getSettings()));
    }
    hudSettings.put("components", componentSettings);
    for (Setting<?> setting : this.settings) {
      hudSettings.put(setting.getName(), setting.getValue());
    }
    return hudSettings;
  }

  @Override
  public void setSettings(Object settings) {
    Map<String, Object> settingsMap = (Map<String, Object>) settings;
    for (Setting<?> setting : this.settings) {
      if (settingsMap.get(setting.getName()) != null) {
        setting.setValueIgnoreType(settingsMap.get(setting.getName()));
      }
    }
    List<Pair<Class<? extends Component>, Map<String, String>>> componentSettings =
        (List<Pair<Class<? extends Component>, Map<String, String>>>) settingsMap.get("components");
    for (Pair<Class<? extends Component>, Map<String, String>> pair : componentSettings) {
      try {
        Component component = pair.getLeft().newInstance();
        component.setSettings(pair.getRight());
        this.addComponent(component);
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public String getSettingsName() {
    return this.getName();
  }

  public List<IComponent> getHUDComponents() {
    return this.components.getValue();
  }

  public void addIconElements(Collection collection) {
    if (this.components.getValue().size() > 0) {
      this.components.getValue().get(0).addIconElements(collection);
    }
  }
}

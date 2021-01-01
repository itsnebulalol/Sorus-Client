package org.sorus.client.module.impl.potionstatus;

import java.awt.*;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.game.IPotionEffect;

public class PotionStatusComponent extends Component {

  private final Setting<Color> backgroundColor;
  private final Setting<Color> nameAmplifierColor;
  private final Setting<Color> durationColor;

  private List<IPotionEffect> potionEffects;

  private double width, height;

  public PotionStatusComponent() {
    super("Potion Status");
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    this.register(nameAmplifierColor = new Setting<>("nameAmplifierColor", Color.WHITE));
    this.register(durationColor = new Setting<>("durationColor", new Color(175, 175, 175)));
  }

  @Override
  public void update(boolean dummy) {
    this.potionEffects =
        Sorus.getSorus().getVersion().getData(IGame.class).getPlayer().getActivePotionEffects();
    if (potionEffects.isEmpty() && dummy) {
      potionEffects = Sorus.getSorus().getVersion().getData(IGame.class).getDummyEffects();
    }
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    Sorus.getSorus()
        .getGUIManager()
        .getRenderer()
        .drawRect(x, y, width, height, backgroundColor.getValue());
    int i = 0;
    for (IPotionEffect potionEffect : potionEffects) {
      potionEffect.drawIcon(x + 2, y + i * 23 + 4, 20, 20);
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .getMinecraftFontRenderer()
          .drawString(
              potionEffect.getName() + " " + potionEffect.getAmplifier(),
              x + 25,
              y + i * 23 + 5,
              1,
              1,
              false,
              this.nameAmplifierColor.getValue());
      Sorus.getSorus()
          .getGUIManager()
          .getRenderer()
          .getMinecraftFontRenderer()
          .drawString(
              potionEffect.getDuration(),
              x + 25,
              y + i * 23 + 16,
              1,
              1,
              false,
              this.durationColor.getValue());
      i++;
    }
  }

  @Override
  public double getWidth() {
    double width = 0;
    for (IPotionEffect potionEffect : potionEffects) {
      width =
          Math.max(
              width,
              Sorus.getSorus()
                      .getGUIManager()
                      .getRenderer()
                      .getMinecraftFontRenderer()
                      .getStringWidth(potionEffect.getName() + " " + potionEffect.getAmplifier())
                  + 27);
    }
    return this.width = width;
  }

  @Override
  public double getHeight() {
    return this.height = potionEffects.size() * 23 + 4;
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/potionstatus/logo.png").size(80, 80));
  }

  @Override
  public String getDescription() {
    return "Displays active potion effects.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
    collection.add(new ColorPicker(nameAmplifierColor, "Name Color"));
    collection.add(new ColorPicker(durationColor, "Duration Color"));
    super.addConfigComponents(collection);
  }
}

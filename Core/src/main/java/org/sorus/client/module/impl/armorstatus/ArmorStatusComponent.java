package org.sorus.client.module.impl.armorstatus;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.Item;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;
import org.sorus.client.version.game.IItemStack;

public class ArmorStatusComponent extends Component {

  private final Setting<Boolean> rawDurability;
  private final Setting<Boolean> showHelmet;
  private final Setting<Boolean> showChestplate;
  private final Setting<Boolean> showLeggings;
  private final Setting<Boolean> showBoots;
  private final Setting<Color> textColor;
  private final Setting<Color> backgroundColor;

  private final Panel modPanel;
  private final Collection mainCollection;
  private final Rectangle background;

  private double width, height;

  public ArmorStatusComponent() {
    super("Armor Status");
    this.register(rawDurability = new Setting<>("rawDurability", false));
    this.register(showHelmet = new Setting<>("showHelment", true));
    this.register(showChestplate = new Setting<>("showChestplate", true));
    this.register(showLeggings = new Setting<>("showLeggings", true));
    this.register(showBoots = new Setting<>("showBoots", true));
    this.register(textColor = new Setting<>("textColor", Color.WHITE));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    this.modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    this.modPanel.add(mainCollection = new Collection());
  }

  @Override
  public void update(boolean dummy) {
    List<IItemStack> armor =
        Sorus.getSorus().getVersion().getData(IGame.class).getPlayer().getInventory().getArmor();
    if (armor.isEmpty() && dummy) {
      armor = Sorus.getSorus().getVersion().getData(IGame.class).getDummyArmor();
    }
    Collections.reverse(armor);
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    this.mainCollection.clear();
    this.width = 0;
    this.height = 0;
    int i = 0;
    for (IItemStack iItemStack : armor) {
      if (this.showArmor(iItemStack)) {
        SingleArmorComponent singleArmorComponent =
            new SingleArmorComponent(iItemStack).position(2, 2 + i * 18);
        mainCollection.add(singleArmorComponent);
        width = Math.max(width, singleArmorComponent.width + 2);
        height += 18;
        i++;
      }
    }
    width += 4;
    height += 4;
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    this.modPanel.position(x, y);
    if (this.mainCollection.getChildren().size() > 0) {
      this.modPanel.onRender();
    }
  }

  private boolean showArmor(IItemStack iItemStack) {
    switch (Sorus.getSorus()
        .getVersion()
        .getData(IGame.class)
        .getItemManager()
        .getArmorType(iItemStack)) {
      case HELMET:
        return this.showHelmet.getValue();
      case CHESTPLATE:
        return this.showChestplate.getValue();
      case LEGGINGS:
        return this.showLeggings.getValue();
      case BOOTS:
        return this.showBoots.getValue();
    }
    return false;
  }

  @Override
  public double getWidth() {
    return this.width;
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  public String getDescription() {
    return "Displays your current armor.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(rawDurability, "Raw Durability"));
    collection.add(new Toggle(showHelmet, "Helmet"));
    collection.add(new Toggle(showChestplate, "Chestplate"));
    collection.add(new Toggle(showLeggings, "Leggings"));
    collection.add(new Toggle(showBoots, "Boots"));
    collection.add(new ColorPicker(textColor, "Text Color"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  public class SingleArmorComponent extends Collection {

    private final IItemStack iItemStack;
    private final Text text;

    private double width;

    public SingleArmorComponent(IItemStack iItemStack) {
      this.iItemStack = iItemStack;
      this.add(new Item().itemStack(iItemStack).position(0, 2));
      this.add(
          text =
              new Text()
                  .fontRenderer(
                      Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer())
                  .position(20, 6.5));
      this.updateText();
    }

    @Override
    public void onRender() {
      this.updateText();
      super.onRender();
    }

    public void updateText() {
      if (ArmorStatusComponent.this.rawDurability.getValue()) {
        this.text.text(String.valueOf((iItemStack.getMaxDamage() - iItemStack.getDamage())));
      } else {
        this.text.text(
            String.format(
                    "%.2f",
                    (iItemStack.getMaxDamage() - iItemStack.getDamage())
                        / (double) iItemStack.getMaxDamage()
                        * 100)
                + "%");
      }

      this.text.color(ArmorStatusComponent.this.textColor.getValue());
      this.width = this.text.width() + 20;
    }
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/armorstatus/logo.png").size(80, 80));
  }
}

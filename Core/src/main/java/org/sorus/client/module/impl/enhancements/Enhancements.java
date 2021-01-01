package org.sorus.client.module.impl.enhancements;

import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.GuiSwitchEvent;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.render.IRenderer;

public class Enhancements extends ModuleConfigurable {

  private final Setting<Boolean> fullBright;
  private final Setting<Boolean> centeredInventory;
  private final Setting<Boolean> guiBlur;
  private final Setting<Double> customFireHeight;
  private final Setting<Double> customFireOpacity;

  private boolean blurring;

  public Enhancements() {
    super("Enhancements");
    this.register(fullBright = new Setting<>("fullBright", false));
    this.register(centeredInventory = new Setting<>("centeredInventory", false));
    this.register(guiBlur = new Setting<>("guiBlur", false));
    this.register(customFireHeight = new Setting<>("customFireHeight", 0.0));
    this.register(customFireOpacity = new Setting<>("customFireOpacity", 0.9));
    Sorus.getSorus().getEventManager().register(this);
  }

  @EventInvoked
  public void onGuiSwitch(GuiSwitchEvent e) {
    if (!this.guiBlur.getValue() || !this.isEnabled()) {
      return;
    }
    switch (e.getType()) {
      case BLANK:
        break;
      case INVENTORY:
      case INVENTORY_CREATIVE:
      case CRAFTING:
        Sorus.getSorus().getVersion().getData(IRenderer.class).enableBlur(7.5);
        blurring = true;
        break;
      default:
        Sorus.getSorus().getVersion().getData(IRenderer.class).disableBlur();
        blurring = false;
        break;
    }
  }

  @EventInvoked
  public void onDrawGradientRect(RenderObjectEvent.GradientRectangle e) {
    if (blurring && e.getLeft() == 0 && e.getTop() == 0) {
      e.setCancelled(true);
    }
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Toggle(fullBright, "Fullbright"));
    collection.add(new Toggle(centeredInventory, "Centered Inventory"));
    collection.add(new Toggle(guiBlur, "Gui Blur"));
    collection.add(new Slider(customFireHeight, -0.5, 0, "Custom Fire Height"));
    collection.add(new Slider(customFireOpacity, 0, 1, "Custom Fire Opacity"));
  }

  public boolean shouldCenterInventory() {
    return this.centeredInventory.getValue();
  }

  public double getCustomFireHeight() {
    return this.customFireHeight.getValue();
  }

  public double getCustomFireOpacity() {
    return this.customFireOpacity.getValue();
  }

  public boolean fullBright() {
    return this.isEnabled() && this.fullBright.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Small tweaks that will enhance and better your game.";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/enhancements/logo.png").size(80, 80));
  }
}

package org.sorus.client.module.impl.blockoverlay;

import java.awt.*;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.render.RenderObjectEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Slider;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.IGLHelper;

public class BlockOverlay extends ModuleConfigurable {

  private final Setting<Double> thickness;
  private final Setting<Color> color;

  public BlockOverlay() {
    super("Block Overlay");
    this.register(thickness = new Setting<>("borderThickness", 2.0));
    this.register(color = new Setting<>("color", Color.BLACK));
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new Slider(thickness, 1, 15, "Outline Thickness"));
    collection.add(new ColorPicker(color, "Outline Color"));
  }

  @EventInvoked
  public void onRenderBlockOverlay(RenderObjectEvent.BlockOverlay e) {
    if (this.isEnabled()) {
      Color color = this.getColor();
      Sorus.getSorus()
          .getVersion()
          .getData(IGLHelper.class)
          .color(
              color.getRed() / 255.0,
              color.getGreen() / 255.0,
              color.getBlue() / 255.0,
              color.getAlpha() / 255.0);
      Sorus.getSorus().getVersion().getData(IGLHelper.class).lineWidth(this.getBorderThickness());
    }
  }

  @EventInvoked
  public void onRenderText(RenderObjectEvent.Text e) {
    if (e.getText().contains("Copyright")) {
      e.setCancelled(true);
    }
  }

  public double getBorderThickness() {
    return thickness.getValue();
  }

  public Color getColor() {
    return color.getValue();
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }

  @Override
  public String getDescription() {
    return "Customize the block overlay to be as colorful as you want!";
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(
        new Image()
            .resource("sorus/modules/blockoverlay/logo.png")
            .size(80, 80)
            .color(new Color(175, 175, 175)));
  }
}

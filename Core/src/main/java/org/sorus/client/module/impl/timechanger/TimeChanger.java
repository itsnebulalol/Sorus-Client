package org.sorus.client.module.impl.timechanger;

import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.module.ModuleConfigurable;
import org.sorus.client.module.VersionDecision;
import org.sorus.client.settings.Setting;

public class TimeChanger extends ModuleConfigurable {

  private final Setting<Double> time;

  public TimeChanger() {
    super("Time Changer");
    this.register(time = new Setting<>("time", 0.25));
  }

  public long getTime() {
    return (long) (time.getValue() * 24000);
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(
        new Image()
            .resource("sorus/modules/timechanger/logo_moon.png")
            .size(50, 50)
            .position(30, 30));
    collection.add(new Image().resource("sorus/modules/timechanger/logo_sun.png").size(50, 50));
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new TimeSlider(time, 0.25, 0.75, "Time"));
    super.addConfigComponents(collection);
  }

  @Override
  public String getDescription() {
    return "Change the sky to any time you want.";
  }

  @Override
  public VersionDecision getVersions() {
    return new VersionDecision.Allow();
  }
}

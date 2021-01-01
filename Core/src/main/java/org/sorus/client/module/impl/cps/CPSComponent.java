package org.sorus.client.module.impl.cps;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.event.EventInvoked;
import org.sorus.client.event.impl.client.input.MousePressEvent;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.MultiText;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.input.Button;

public class CPSComponent extends Component {

  private final List<CPSMode> registeredModes = new ArrayList<>();
  private final List<String> registeredModeNames = new ArrayList<>();
  private CPSMode currentMode;

  private IFontRenderer fontRenderer;

  private final Setting<Long> mode;
  private final Setting<Boolean> customFont;
  private final Setting<Color> backgroundColor;

  private final Panel modPanel;
  private final Rectangle background;
  private final Collection cpsText;

  private final Map<Button, List<Long>> prevClickTimes = new HashMap<>();
  private String[] cpsString = new String[0];

  public CPSComponent() {
    super("CPS");
    this.register(new CPSMode.LabelPreMode());
    this.register(new CPSMode.LabelPostMode());
    this.register(new CPSMode.BothButtonsMode());
    this.register(new CPSMode.CustomMode());
    this.register(
        mode =
            new Setting<Long>("mode", 0L) {
              @Override
              public void setValue(Long value) {
                CPSComponent.this.setMode(registeredModes.get(value.intValue()));
                super.setValue(value);
              }
            });
    this.register(customFont = new Setting<>("customFont", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(cpsText = new Collection());
    this.currentMode = this.registeredModes.get(0);
    this.updateFontRenderer();
    Sorus.getSorus().getEventManager().register(this);
    for (Button button : Button.values()) {
      this.prevClickTimes.put(button, new ArrayList<>());
    }
  }

  @Override
  public void update(boolean dummy) {
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    long currentTime = System.currentTimeMillis();
    for (List<Long> prevClickTimes : this.prevClickTimes.values()) {
      prevClickTimes.removeIf((value) -> currentTime - value > 1000);
    }
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    int i = 0;
    Map<Integer, Integer> cps = new HashMap<>();
    for (Button button : this.prevClickTimes.keySet()) {
      cps.put(button.ordinal(), this.prevClickTimes.get(button).size());
    }
    List<List<Pair<String, Color>>> formatted = this.currentMode.format(cps);
    List<String> strings = new ArrayList<>();
    for (List<Pair<String, Color>> formattedLine : formatted) {
      StringBuilder cpsBuilder = new StringBuilder();
      if (i >= this.cpsText.getChildren().size()) {
        MultiText multiText = new MultiText();
        this.cpsText.add(multiText);
      }
      MultiText multiText = (MultiText) this.cpsText.getChildren().get(i);
      int j = 0;
      for (Pair<String, Color> pair : formattedLine) {
        if (j >= multiText.getChildren().size()) {
          Text text = new Text();
          multiText.add(text);
        }
        Text text = (Text) multiText.getChildren().get(j);
        text.text(pair.getKey()).fontRenderer(fontRenderer).color(pair.getValue());
        j++;
        cpsBuilder.append(pair.getLeft());
      }
      if (multiText.getChildren().size() > formattedLine.size()) {
        Text text1 = (Text) multiText.getChildren().get(multiText.getChildren().size() - 1);
        multiText.remove(text1);
      }
      double specificHeight = this.getHeight() / this.cpsText.getChildren().size();
      double textY = specificHeight * i + specificHeight / 2 - multiText.getHeight() / 2;
      multiText.position(this.getWidth() / 2 - multiText.getWidth() / 2, textY);
      strings.add(cpsBuilder.toString());
      i++;
    }
    if (this.cpsText.getChildren().size() > formatted.size()) {
      MultiText multiText1 =
          (MultiText) this.cpsText.getChildren().get(this.cpsText.getChildren().size() - 1);
      cpsText.remove(multiText1);
    }
    this.cpsString = strings.toArray(new String[0]);
    this.modPanel.position(x, y);
    this.modPanel.onRender();
  }

  private void updateFontRenderer() {
    if (customFont.getValue()) {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getRubikFontRenderer();
    } else {
      fontRenderer = Sorus.getSorus().getGUIManager().getRenderer().getMinecraftFontRenderer();
    }
  }

  public void register(CPSMode mode) {
    this.registeredModes.add(mode);
    this.registeredModeNames.add(mode.getName());
  }

  public void setMode(CPSMode mode) {
    if (this.currentMode != null) {
      for (Setting<?> setting : this.currentMode.getSettings()) {
        this.unregister(setting);
      }
    }
    this.currentMode = mode;
    for (Setting<?> setting : this.currentMode.getSettings()) {
      this.register(setting);
    }
    this.cpsText.clear();
  }

  @Override
  public double getWidth() {
    return this.currentMode.getWidth(this.cpsString, this.fontRenderer);
  }

  @Override
  public double getHeight() {
    return this.currentMode.getHeight(this.cpsString, this.fontRenderer);
  }

  @Override
  public String getDescription() {
    return "Displays CPS.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, registeredModeNames, "Mode"));
    this.currentMode.addConfigComponents(collection);
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  @EventInvoked
  public void onMouseClick(MousePressEvent e) {
    long tick = System.currentTimeMillis();
    prevClickTimes.get(e.getButton()).add(tick);
  }
}

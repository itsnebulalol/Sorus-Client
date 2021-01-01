package org.sorus.client.module.impl.ping;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.Image;
import org.sorus.client.gui.core.component.impl.MultiText;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ClickThrough;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;
import org.sorus.client.version.game.IGame;

public class PingComponent extends Component {

  private final List<PingMode> registeredModes = new ArrayList<>();
  private final List<String> registeredModeNames = new ArrayList<>();
  private PingMode currentMode;

  private IFontRenderer fontRenderer;

  private final Setting<Long> mode;
  private final Setting<Boolean> customFont;
  private final Setting<Boolean> tightFit;
  private final Setting<Color> backgroundColor;

  private final Panel modPanel;
  private final Rectangle background;
  private final Collection pingText;

  private String[] pingString = new String[0];

  public PingComponent() {
    super("Ping");
    this.register(new PingMode.LabelPreMode());
    this.register(new PingMode.LabelPostMode());
    this.register(new PingMode.CustomMode());
    this.register(
        mode =
            new Setting<Long>("mode", 0L) {
              @Override
              public void setValue(Long value) {
                PingComponent.this.setMode(registeredModes.get(value.intValue()));
                super.setValue(value);
              }
            });
    this.register(customFont = new Setting<>("customFont", false));
    this.register(tightFit = new Setting<>("tightFit", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(pingText = new Collection());
    this.currentMode = this.registeredModes.get(0);
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    this.updateFontRenderer();
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    int i = 0;
    List<List<Pair<String, Color>>> formatted =
        this.currentMode.format(Sorus.getSorus().getVersion().getData(IGame.class).getPing());
    List<String> strings = new ArrayList<>();
    for (List<Pair<String, Color>> formattedLine : formatted) {
      StringBuilder fpsBuilder = new StringBuilder();
      if (i >= this.pingText.getChildren().size()) {
        MultiText multiText = new MultiText();
        this.pingText.add(multiText);
      }
      MultiText multiText = (MultiText) this.pingText.getChildren().get(i);
      int j = 0;
      for (Pair<String, Color> pair : formattedLine) {
        if (j >= multiText.getChildren().size()) {
          Text text = new Text();
          multiText.add(text);
        }
        Text text = (Text) multiText.getChildren().get(j);
        text.text(pair.getKey()).fontRenderer(fontRenderer).color(pair.getValue());
        j++;
        fpsBuilder.append(pair.getLeft());
      }
      if (multiText.getChildren().size() > formattedLine.size()) {
        Text text1 = (Text) multiText.getChildren().get(multiText.getChildren().size() - 1);
        multiText.remove(text1);
      }
      double specificHeight = this.getHeight() / this.pingText.getChildren().size();
      double textY = specificHeight * i + specificHeight / 2 - multiText.getHeight() / 2;
      multiText.position(this.getWidth() / 2 - multiText.getWidth() / 2, textY);
      strings.add(fpsBuilder.toString());
      i++;
    }
    if (this.pingText.getChildren().size() > formatted.size()) {
      MultiText multiText1 =
          (MultiText) this.pingText.getChildren().get(this.pingText.getChildren().size() - 1);
      pingText.remove(multiText1);
    }
    this.pingString = strings.toArray(new String[0]);
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

  public void register(PingMode mode) {
    this.registeredModes.add(mode);
    this.registeredModeNames.add(mode.getName());
  }

  public void setMode(PingMode mode) {
    if (this.currentMode != null) {
      for (Setting<?> setting : this.currentMode.getSettings()) {
        this.unregister(setting);
      }
    }
    this.currentMode = mode;
    for (Setting<?> setting : this.currentMode.getSettings()) {
      this.register(setting);
    }
    this.pingText.clear();
  }

  @Override
  public double getWidth() {
    if (tightFit.getValue()) {
      double maxWidth = 0;
      for (String string : pingString) {
        maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
      }
      return maxWidth + 4;
    }
    return 60;
  }

  @Override
  public double getHeight() {
    return tightFit.getValue()
        ? fontRenderer.getFontHeight() * this.pingText.getChildren().size()
            + (this.pingText.getChildren().size() - 1) * 2
            + 4
        : 11;
  }

  @Override
  public String getDescription() {
    return "Displays Ping.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new ClickThrough(mode, registeredModeNames, "Mode"));
    this.currentMode.addConfigComponents(collection);
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new Toggle(tightFit, "Tight Fit"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }

  @Override
  public void addIconElements(Collection collection) {
    collection.add(new Image().resource("sorus/modules/ping/logo.png").size(80, 80));
  }
}

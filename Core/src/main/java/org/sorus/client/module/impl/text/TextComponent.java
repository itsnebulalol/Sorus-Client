package org.sorus.client.module.impl.text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Collection;
import org.sorus.client.gui.core.component.Panel;
import org.sorus.client.gui.core.component.impl.MultiText;
import org.sorus.client.gui.core.component.impl.Rectangle;
import org.sorus.client.gui.core.component.impl.Text;
import org.sorus.client.gui.core.font.IFontRenderer;
import org.sorus.client.gui.hud.Component;
import org.sorus.client.gui.screen.settings.components.ColorPicker;
import org.sorus.client.gui.screen.settings.components.CustomTextColor;
import org.sorus.client.gui.screen.settings.components.Toggle;
import org.sorus.client.settings.Setting;

public class TextComponent extends Component {

  private IFontRenderer fontRenderer;

  private final Setting<List<List<Pair<String, Color>>>> text;
  private final Setting<Boolean> customFont;
  private final Setting<Color> backgroundColor;

  private final Panel modPanel;
  private final Rectangle background;
  private final Collection textText;

  private String[] textString = new String[0];

  public TextComponent() {
    super("Text");
    this.register(text = new Setting<>("text", new ArrayList<>()));
    this.register(customFont = new Setting<>("customFont", false));
    this.register(backgroundColor = new Setting<>("backgroundColor", new Color(0, 0, 0, 50)));
    modPanel = new Panel();
    modPanel.add(background = new Rectangle());
    modPanel.add(textText = new Collection());
    this.updateFontRenderer();
    Sorus.getSorus().getEventManager().register(this);
  }

  @Override
  public void update(boolean dummy) {
    this.updateFontRenderer();
  }

  @Override
  public void render(double x, double y, boolean dummy) {
    this.background.size(this.hud.getWidth(), this.getHeight()).color(backgroundColor.getValue());
    int i = 0;
    List<List<Pair<String, Color>>> formatted = this.text.getValue();
    List<String> strings = new ArrayList<>();
    for (List<Pair<String, Color>> formattedLine : formatted) {
      StringBuilder cpsBuilder = new StringBuilder();
      if (i >= this.textText.getChildren().size()) {
        MultiText multiText = new MultiText();
        this.textText.add(multiText);
      }
      MultiText multiText = (MultiText) this.textText.getChildren().get(i);
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
      double specificHeight = this.getHeight() / this.textText.getChildren().size();
      double textY = specificHeight * i + specificHeight / 2 - multiText.getHeight() / 2;
      multiText.position(this.getWidth() / 2 - multiText.getWidth() / 2, textY);
      strings.add(cpsBuilder.toString());
      i++;
    }
    if (this.textText.getChildren().size() > formatted.size()) {
      MultiText multiText1 =
          (MultiText) this.textText.getChildren().get(this.textText.getChildren().size() - 1);
      textText.remove(multiText1);
    }
    this.textString = strings.toArray(new String[0]);
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

  @Override
  public double getWidth() {
    double maxWidth = 0;
    for (String string : textString) {
      maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(string));
    }
    return maxWidth + 4;
  }

  @Override
  public double getHeight() {
    return fontRenderer.getFontHeight() * textString.length + (textString.length - 1) * 2 + 4;
  }

  @Override
  public String getDescription() {
    return "Displays CPS.";
  }

  @Override
  public void addConfigComponents(Collection collection) {
    collection.add(new CustomTextColor(text, "Text"));
    collection.add(new Toggle(customFont, "Custom Font"));
    collection.add(new ColorPicker(backgroundColor, "Background Color"));
  }
}

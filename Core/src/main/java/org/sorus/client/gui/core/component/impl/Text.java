package org.sorus.client.gui.core.component.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.gui.core.component.api.IScreenRenderer;
import org.sorus.client.gui.core.font.IFontRenderer;

public class Text extends Component {

  private IFontRenderer fontRenderer;
  private String text = "";
  private boolean shadow;

  @Override
  public void onRender() {
    IScreenRenderer renderer = Sorus.getSorus().getGUIManager().getRenderer();
    renderer.drawString(
        fontRenderer,
        text,
        this.absoluteX(),
        this.absoluteY(),
        this.absoluteXScale(),
        this.absoluteYScale(),
        shadow,
        this.absoluteColor());
  }

  public <T extends Text> T fontRenderer(IFontRenderer fontRenderer) {
    this.fontRenderer = fontRenderer;
    return this.cast();
  }

  public IFontRenderer fontRenderer() {
    return this.fontRenderer;
  }

  public <T extends Text> T text(String text) {
    this.text = text;
    return this.cast();
  }

  public <T extends Text> T shadow(boolean shadow) {
    this.shadow = shadow;
    return this.cast();
  }

  public double width() {
    return fontRenderer.getStringWidth(text);
  }

  public double height() {
    return fontRenderer.getFontHeight();
  }

  public String text() {
    return this.text;
  }
}

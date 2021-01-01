package org.sorus.client.gui.core.component.impl;

import org.sorus.client.Sorus;
import org.sorus.client.gui.core.component.Component;
import org.sorus.client.version.game.IItemStack;

public class Item extends Component {

  private IItemStack iItemStack;

  @Override
  public void onRender() {
    Sorus.getSorus()
        .getGUIManager()
        .getRenderer()
        .drawItem(this.iItemStack, this.absoluteX(), this.absoluteY(), this.absoluteColor());
  }

  public <T extends Item> T itemStack(IItemStack iItemStack) {
    this.iItemStack = iItemStack;
    return this.cast();
  }
}

package org.sorus.client.version.input;

import org.sorus.client.Sorus;

public enum Button implements Input {
  ONE,
  NULL;

  @Override
  public boolean isDown() {
    return Sorus.getSorus().getVersion().getData(IInput.class).isButtonDown(this);
  }

  @Override
  public String getName() {
    return this.name();
  }
}

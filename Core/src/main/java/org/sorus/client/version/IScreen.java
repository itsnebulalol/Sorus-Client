package org.sorus.client.version;

import java.io.InputStream;

public interface IScreen {

  double getScaledWidth();

  double getScaledHeight();

  double getScaleFactor();

  double getDisplayWidth();

  double getDisplayHeight();

  void setIcon(InputStream x16);

  void setTitle(String title);
}

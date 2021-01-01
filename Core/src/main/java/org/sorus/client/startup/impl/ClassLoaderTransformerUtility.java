package org.sorus.client.startup.impl;

import java.net.URLClassLoader;
import org.sorus.client.startup.TransformerUtility;

public class ClassLoaderTransformerUtility extends TransformerUtility {

  private URLClassLoader classLoader;

  @Override
  public void initialize() {}

  public byte[] transform(String className, byte[] data, ClassLoader classLoader) {
    return this.transformer.transform(className, data, classLoader, null);
  }

  public void setClassLoader(URLClassLoader classLoader) {
    this.classLoader = classLoader;
  }
}

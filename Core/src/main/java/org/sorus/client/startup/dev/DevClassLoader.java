package org.sorus.client.startup.dev;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import org.sorus.client.startup.impl.ClassLoaderTransformerUtility;

public class DevClassLoader extends URLClassLoader {

  private final ClassLoaderTransformerUtility transformerUtility;

  public DevClassLoader(URL[] urls, ClassLoaderTransformerUtility transformerUtility) {
    super(urls, null);
    this.transformerUtility = transformerUtility;
    transformerUtility.setClassLoader(this);
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] bt = loadClassData(name);
    if (bt == null) {
      return super.findClass(name);
    }
    bt = transformerUtility.transform(name.replace(".", "/"), bt, this);
    return defineClass(name, bt, 0, bt.length);
  }

  private byte[] loadClassData(String className) {
    InputStream is =
        getClass().getClassLoader().getResourceAsStream(className.replace(".", "/") + ".class");
    if (is == null) {
      return null;
    }
    ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
    int len;
    try {
      while ((len = is.read()) != -1) {
        byteSt.write(len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return byteSt.toByteArray();
  }
}

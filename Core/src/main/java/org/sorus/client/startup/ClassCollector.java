package org.sorus.client.startup;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public class ClassCollector implements ClassFileTransformer {

  private static final Map<String, byte[]> originalClasses = new HashMap<>();

  private static final Map<String, byte[]> classes = new HashMap<>();

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    if (!originalClasses.containsKey(className)) {
      originalClasses.put(className, classfileBuffer);
      classes.put(className, classfileBuffer);
    }
    return new byte[0];
  }

  public static byte[] getOriginalData(String className) {
    return originalClasses.get(className);
  }

  public static byte[] getData(String className) {
    return classes.get(className);
  }

  public static void setData(String className, byte[] data) {
    classes.put(className, data);
  }
}

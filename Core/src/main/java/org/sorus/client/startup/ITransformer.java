package org.sorus.client.startup;

public interface ITransformer {

  byte[] transform(String name, byte[] bytes, ClassLoader classLoader, String resourcePath);
}

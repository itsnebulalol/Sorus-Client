package org.sorus.client.startup;

import java.security.ProtectionDomain;

public interface ITransformer {

  byte[] transform(
      String name, byte[] bytes, ClassLoader classLoader, ProtectionDomain protectionDomain);
}

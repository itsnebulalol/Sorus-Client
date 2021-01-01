package org.sorus.client.startup.injection.type;

public interface IInjectionType<T, U> {

  byte[] inject(byte[] data, T type, Class<?> injector, U holder);
}

package org.sorus.client.version;

public interface IVersion {

  <T> T getData(Class<T> clazz);
}

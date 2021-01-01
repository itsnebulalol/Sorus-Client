package org.sorus.client.startup.injection;

public class Injector<T> {

  protected final T that;

  public Injector(T that) {
    this.that = that;
  }
}

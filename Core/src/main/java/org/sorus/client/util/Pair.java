package org.sorus.client.util;

public class Pair<U, V> {

  private final U left;
  private final V right;

  public Pair(U left, V right) {
    this.left = left;
    this.right = right;
  }

  public static <U, V> Pair<U, V> of(U left, V right) {
    return new Pair<>(left, right);
  }

  public U getLeft() {
    return left;
  }

  public V getRight() {
    return right;
  }

  public U getKey() {
    return left;
  }

  public V getValue() {
    return right;
  }
}

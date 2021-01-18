package org.sorus.client.util;

public class MathUtil {

  public static double clamp(double value, double lower, double upper) {
    return Math.min(upper, Math.max(lower, value));
  }

  public static double average(double val1, double val2) {
    return (val1 + val2) / 2;
  }

  public static double getBetween(double value1, double value2, double percentBetween) {
    return (value2 - value1) * percentBetween + value1;
  }
}

package org.sorus.client.startup;

public class TransformerUtility {

  protected ITransformer transformer;

  public void initialize() {}

  public void addTransformer(ITransformer transformer) {
    this.transformer = transformer;
  }
}

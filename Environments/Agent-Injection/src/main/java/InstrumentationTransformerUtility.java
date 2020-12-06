/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.TransformerUtility;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * Implementation of {@link TransformerUtility} for a java agent injection with {@link
 * Instrumentation}.
 */
public class InstrumentationTransformerUtility extends TransformerUtility
    implements ClassFileTransformer {

  private final Instrumentation instrumentation;

  public InstrumentationTransformerUtility(Instrumentation instrumentation) {
    this.instrumentation = instrumentation;
  }

  /** Adds itself to the transformers of the instrumentation. */
  @Override
  public void initialize() {
    instrumentation.addTransformer(this, true);
    try {
      instrumentation.retransformClasses(SorusStartup.class);
    } catch (UnmodifiableClassException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calls the transformer to transform the class.
   *
   * @param loader the class loader
   * @param className the class' name
   * @param classBeingRedefined the class being transformed
   * @param protectionDomain the protection domain of the class
   * @param classfileBuffer the class byte code
   * @return the modified class byte code
   */
  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    classfileBuffer =
        this.transformer.transform(className, classfileBuffer, loader, protectionDomain);
    return classfileBuffer;
  }
}

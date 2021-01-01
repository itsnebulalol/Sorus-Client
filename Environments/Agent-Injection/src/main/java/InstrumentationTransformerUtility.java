

import org.sorus.client.startup.SorusStartup;
import org.sorus.client.startup.TransformerUtility;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;


public class InstrumentationTransformerUtility extends TransformerUtility
    implements ClassFileTransformer {

  private final Instrumentation instrumentation;

  public InstrumentationTransformerUtility(Instrumentation instrumentation) {
    this.instrumentation = instrumentation;
  }

  
  @Override
  public void initialize() {
    instrumentation.addTransformer(this, true);
    try {
      instrumentation.retransformClasses(SorusStartup.class);
    } catch (UnmodifiableClassException e) {
      e.printStackTrace();
    }
  }

  
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

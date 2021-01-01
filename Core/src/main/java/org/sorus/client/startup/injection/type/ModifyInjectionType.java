package org.sorus.client.startup.injection.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Modify;

public class ModifyInjectionType implements IMethodInjectionType<Modify> {

  @Override
  public byte[] inject(byte[] data, Modify modify, Class<?> injector, Method method) {
    ClassReader classReader = new ClassReader(data);
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);
    String name =
        ObfuscationManager.getMethodName(
            injector.getDeclaredAnnotation(Hook.class).value(), modify.name(), modify.desc());
    String desc = ObfuscationManager.getDesc(modify.desc());
    for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {
      if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
        method.setAccessible(true);
        try {
          method.invoke(null, methodNode);
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
        break;
      }
    }
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    classNode.accept(classWriter);
    return classWriter.toByteArray();
  }
}

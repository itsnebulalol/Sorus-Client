package org.sorus.client.startup.injection.type;

import java.lang.reflect.Method;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.InjectionManager;
import org.sorus.client.startup.injection.inject.At;
import org.sorus.client.startup.injection.inject.Inject;

public class InjectInjectionType implements IMethodInjectionType<Inject> {

  @Override
  public byte[] inject(byte[] data, Inject inject, Class<?> injector, Method method) {
    ClassReader classReader = new ClassReader(data);
    ClassNode classNode = new ClassNode();
    classReader.accept(classNode, 0);
    String name =
        ObfuscationManager.getMethodName(
            injector.getDeclaredAnnotation(Hook.class).value().replace(".", "/"),
            inject.name(),
            inject.desc());
    String desc = ObfuscationManager.getDesc(inject.desc());
    method.setAccessible(true);
    for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {
      if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
        this.injectAtLocation(methodNode, injector, method, inject.at());
        break;
      }
    }
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    classNode.accept(classWriter);
    return classWriter.toByteArray();
  }

  private void injectAtLocation(MethodNode methodNode, Class<?> injector, Method method, At at) {
    if (at.value().equals("HEAD")) {
      methodNode.instructions.insert(this.getList(methodNode, injector, method));
    } else {
      int ordinal = 0;
      for (AbstractInsnNode node : methodNode.instructions.toArray()) {
        switch (at.value()) {
          case "RETURN":
            {
              if (node.getOpcode() == Opcodes.RETURN) {
                if (at.ordinal() == -1 || at.ordinal() == ordinal) {
                  methodNode.instructions.insertBefore(
                      node, this.getList(methodNode, injector, method));
                }
                ordinal++;
              }
              break;
            }
          case "INVOKE":
            {
              if (node instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                String[] strings = at.target().split(";");
                String owner = ObfuscationManager.getClassName(strings[0]);
                String name = strings[1].substring(0, strings[1].indexOf("("));
                String desc = strings[1].substring(strings[1].indexOf("("));
                name = ObfuscationManager.getMethodName(strings[0], name, desc);
                desc = ObfuscationManager.getDesc(desc);
                if (methodInsnNode.owner.equals(owner)
                    && methodInsnNode.name.equals(name)
                    && methodInsnNode.desc.equals(desc)) {
                  if (at.ordinal() == -1 || at.ordinal() == ordinal) {
                    if (at.shift() == At.Shift.BEFORE) {
                      methodNode.instructions.insertBefore(
                          node, this.getList(methodNode, injector, method));
                    } else if (at.shift() == At.Shift.AFTER) {
                      methodNode.instructions.insert(
                          node, this.getList(methodNode, injector, method));
                    }
                  }
                  ordinal++;
                }
              }
              break;
            }
        }
      }
    }
  }

  private InsnList getList(MethodNode methodNode, Class<?> injector, Method method) {
    InsnList insnList = new InsnList();
    if ((method.getModifiers() & Opcodes.ACC_STATIC) != 0) {
      insnList.add(
          new MethodInsnNode(
              Opcodes.INVOKESTATIC,
              injector.getCanonicalName().replace(".", "/"),
              method.getName(),
              Type.getType(method).getDescriptor(),
              false));
    } else {
      insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
      insnList.add(new LdcInsnNode(injector.getCanonicalName()));
      insnList.add(
          new MethodInsnNode(
              Opcodes.INVOKESTATIC,
              InjectionManager.class.getCanonicalName().replace(".", "/"),
              "getInjector",
              "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
              false));
      insnList.add(
          new TypeInsnNode(Opcodes.CHECKCAST, injector.getCanonicalName().replace(".", "/")));
      insnList.add(
          new MethodInsnNode(
              Opcodes.INVOKEVIRTUAL,
              injector.getCanonicalName().replace(".", "/"),
              method.getName(),
              Type.getType(method).getDescriptor(),
              false));
    }
    return insnList;
  }
}

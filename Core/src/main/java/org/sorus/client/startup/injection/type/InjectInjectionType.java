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

/** The injection type used with the {@link Inject} annotation. */
public class InjectInjectionType implements IMethodInjectionType<Inject> {

  /**
   * Finds the method that is to being injected into and modifies it with a method call to the
   * annotated method.
   *
   * @param data the data of the class being modified
   * @param inject the inject annotation used to specify the location
   * @param injector the injector class
   * @param method the method that is annotated with {@link Inject}
   * @return the modified class data
   */
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

  /**
   * Injects into the method and modifies it based on the location provided with the {@link At}.
   *
   * @param methodNode the methodnode being modified
   * @param injector the injector
   * @param method the method that the methodnode will now call
   * @param at the location of the injection in the method
   */
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

  /**
   * Returns a new {@link org.objectweb.asm.tree.MethodInsnNode} based on the method that will be
   * called.
   *
   * @param injector the injector
   * @param method the method that is going to be called because of the injection
   * @return the methodinsn node based on the paramaters
   */
  private InsnList getList(MethodNode methodNode, Class<?> injector, Method method) {
    InsnList insnList = new InsnList();
    if ((methodNode.access & Opcodes.ACC_STATIC) != 0) {
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

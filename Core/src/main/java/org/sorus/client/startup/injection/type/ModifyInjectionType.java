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

/** The injection type used with the {@link Modify} annotation. */
public class ModifyInjectionType implements IMethodInjectionType<Modify> {

  /**
   * Finds the method that is to being injected into and gives the {@link
   * org.objectweb.asm.tree.MethodNode} representing it to the method applying the modification.
   *
   * @param data the data of the class being modified
   * @param modify the modify annotation used to specify the method being modified
   * @param injector the injector class
   * @param method the method that is annotated with {@link Modify}
   * @return the modified class data
   */
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

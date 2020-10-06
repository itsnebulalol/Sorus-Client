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

package org.sorus.client.startup;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.obfuscation.mappings.ClassMapping;
import org.sorus.client.obfuscation.mappings.Mappings;
import org.sorus.client.startup.injection.Modify;
import org.sorus.client.startup.injection.inject.Inject;
import org.sorus.client.startup.injection.type.IInjectionType;
import org.sorus.client.startup.injection.type.InjectInjectionType;
import org.sorus.client.startup.injection.type.ModifyInjectionType;

public class MainTransformer implements ITransformer {

  /** A list of the registered {@link IInjectionType} for use when injecting. */
  private final Map<Class<?>, IInjectionType<?, ?>> injectionTypes = new HashMap<>();

  private final Map<String, List<Class<?>>> injectors;

  public MainTransformer(Map<String, List<Class<?>>> injectors) {
    this.injectors = injectors;
    injectionTypes.put(Modify.class, new ModifyInjectionType());
    injectionTypes.put(Inject.class, new InjectInjectionType());
  }

  @Override
  public byte[] transform(
      String name, byte[] data, ClassLoader classLoader, ProtectionDomain protectionDomain) {
    if (name.equals("net/minecraft/client/main/Main")) {
      SorusStartup.classLoader = classLoader;
    }
    if (protectionDomain != null
        && protectionDomain.getCodeSource().getLocation().getFile().contains("sorus")) {
      ClassNode classNode = new ClassNode();
      ClassReader classReader = new ClassReader(data);
      classReader.accept(classNode, 0);
      ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      this.reobfClass(classNode);
      classNode.accept(classWriter);
      data = classWriter.toByteArray();
    }
    List<Class<?>> injectors2 = injectors.get(name);
    if (injectors2 == null) {
      return data;
    }
    System.out.println("Transforming " + name + "...");
    for (Class<?> injector : injectors2) {
      for (Method method : injector.getDeclaredMethods()) {
        if (method.getDeclaredAnnotation(Modify.class) != null) {
          data =
              getInjectionType(Modify.class)
                  .inject(data, method.getDeclaredAnnotation(Modify.class), injector, method);
        }
        if (method.getDeclaredAnnotation(Inject.class) != null) {
          data =
              getInjectionType(Inject.class)
                  .inject(data, method.getDeclaredAnnotation(Inject.class), injector, method);
        }
      }
    }
    return data;
  }

  private void reobfClass(ClassNode classNode) {
    Mappings mappings = ObfuscationManager.externalMappings;
    ClassMapping classMapping = mappings.getClassMapping(classNode.name, 0);

    if (classMapping != null) {
      classNode.name = classMapping.getClassData(1).getName();
    }

    List<String> interfaces = new ArrayList<String>(classNode.interfaces);

    for (String string : interfaces) {
      ClassMapping classMapping1 = mappings.getClassMapping(string, 0);

      if (classMapping1 != null) {
        classNode.interfaces.remove(string);
        classNode.interfaces.add(classMapping1.getClassData(1).getName());
      }
    }

    ClassMapping classMapping1 = mappings.getClassMapping(classNode.superName, 0);

    if (classMapping1 != null) {
      classNode.superName = classMapping1.getClassData(1).getName();
    }

    classNode.signature = formatSignature(classNode.signature, mappings);

    if (classNode.visibleAnnotations != null) {
      for (AnnotationNode node : (List<AnnotationNode>) classNode.visibleAnnotations) {
        if (node.values != null) {
          List<Object> values = new ArrayList<>(node.values);
          for (Object object : values) {
            if (object instanceof Type) {
              Type type = (Type) object;
              ClassMapping classMapping2 =
                  mappings.getClassMapping(formatClassName(type.getClassName()), 0);

              if (classMapping2 != null) {
                node.values.remove(object);
                node.values.add(
                    Type.getType(
                        object
                            .toString()
                            .replace(
                                formatClassName(type.getClassName()),
                                classMapping2.getClassData(1).getName())));
              }
            }
          }
        }
      }
    }

    for (InnerClassNode innerClassNode : (List<InnerClassNode>) classNode.innerClasses) {

      ClassMapping classMapping2 = mappings.getClassMapping(innerClassNode.name, 0);

      if (classMapping2 != null) {
        innerClassNode.name = classMapping2.getClassData(1).getName();
      }

      ClassMapping classMapping3 = mappings.getClassMapping(innerClassNode.outerName, 0);

      if (classMapping3 != null) {
        innerClassNode.outerName = classMapping3.getClassData(1).getName();
      }

      innerClassNode.innerName =
          innerClassNode.name.split("\\$")[innerClassNode.name.split("\\$").length - 1];
    }

    for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {

      for (AbstractInsnNode node : methodNode.instructions.toArray()) {
        if (node instanceof MethodInsnNode) {

          ClassMapping classMapping4 =
              mappings.getClassMapping(formatClassName(((MethodInsnNode) node).owner), 0);

          if (classMapping4 != null) {
            if (classMapping4
                    .getClassData(0)
                    .getMethodData(((MethodInsnNode) node).name, ((MethodInsnNode) node).desc)
                != null) {
              ((MethodInsnNode) node).name =
                  classMapping4
                      .getClassData(1)
                      .getMethodDatas()
                      .get(
                          classMapping4
                              .getClassData(0)
                              .getMethodDatas()
                              .indexOf(
                                  classMapping4
                                      .getClassData(0)
                                      .getMethodData(
                                          ((MethodInsnNode) node).name,
                                          ((MethodInsnNode) node).desc)))
                      .getName();
            }
          }

          Type type = Type.getType(((MethodInsnNode) node).desc);

          for (Type type1 : type.getArgumentTypes()) {
            ClassMapping classMapping2 =
                mappings.getClassMapping(formatClassName(type1.getClassName()), 0);

            if (classMapping2 != null) {
              ((MethodInsnNode) node).desc =
                  ((MethodInsnNode) node)
                      .desc.replace(
                          "L" + formatClassName(type1.getClassName()) + ";",
                          "L" + classMapping2.getClassData(1).getName() + ";");
            }
          }

          Type type1 = type.getReturnType();

          ClassMapping classMapping2 =
              mappings.getClassMapping(formatClassName(type1.getClassName()), 0);

          if (classMapping2 != null) {
            ((MethodInsnNode) node).desc =
                ((MethodInsnNode) node)
                    .desc.replace(
                        "L" + formatClassName(type1.getClassName()) + ";",
                        "L" + classMapping2.getClassData(1).getName() + ";");
          }

          ClassMapping classMapping3 =
              mappings.getClassMapping(formatClassName(((MethodInsnNode) node).owner), 0);

          if (classMapping3 != null) {
            ((MethodInsnNode) node).owner =
                ((MethodInsnNode) node)
                    .owner.replace(
                        formatClassName(((MethodInsnNode) node).owner),
                        classMapping3.getClassData(1).getName());
          }
        }
        if (node instanceof FieldInsnNode) {

          ClassMapping classMapping4 =
              mappings.getClassMapping(formatClassName(((FieldInsnNode) node).owner), 0);

          if (classMapping4 != null) {
            if (classMapping4.getClassData(0).getFieldData(((FieldInsnNode) node).name) != null) {
              ((FieldInsnNode) node).name =
                  classMapping4
                      .getClassData(1)
                      .getFieldDatas()
                      .get(
                          classMapping4
                              .getClassData(0)
                              .getFieldDatas()
                              .indexOf(
                                  classMapping4
                                      .getClassData(0)
                                      .getFieldData(((FieldInsnNode) node).name)))
                      .getName();
            }
          }

          Type type = Type.getType(((FieldInsnNode) node).desc);

          ClassMapping classMapping2 =
              mappings.getClassMapping(formatClassName(type.getClassName()), 0);

          if (classMapping2 != null) {
            ((FieldInsnNode) node).desc =
                ((FieldInsnNode) node)
                    .desc.replace(
                        formatClassName(type.getClassName()),
                        classMapping2.getClassData(1).getName());
          }

          ClassMapping classMapping3 = mappings.getClassMapping(((FieldInsnNode) node).owner, 0);

          if (classMapping3 != null) {
            ((FieldInsnNode) node).owner = classMapping3.getClassData(1).getName();
          }
        }
        if (node instanceof TypeInsnNode) {
          ClassMapping classMapping2 =
              mappings.getClassMapping(formatClassName(((TypeInsnNode) node).desc), 0);

          if (classMapping2 != null) {
            ((TypeInsnNode) node).desc =
                ((TypeInsnNode) node)
                    .desc.replace(
                        formatClassName(((TypeInsnNode) node).desc),
                        classMapping2.getClassData(1).getName());
          }
        }
        if (node instanceof LdcInsnNode) {
          if (((LdcInsnNode) node).cst instanceof Type) {

            ClassMapping classMapping2 =
                mappings.getClassMapping(
                    formatClassName(((Type) ((LdcInsnNode) node).cst).getClassName()), 0);

            if (classMapping2 != null) {
              ((LdcInsnNode) node).cst =
                  Type.getType("L" + classMapping2.getClassData(1).getName() + ";");
            }
          }
        }
        if (node instanceof FrameNode) {

          if (((FrameNode) node).stack != null) {
            for (Object object : ((FrameNode) node).stack) {
              if (object instanceof String) {
                ClassMapping classMapping2 =
                    mappings.getClassMapping(formatClassName((String) object), 0);

                if (classMapping2 != null) {
                  int index = ((FrameNode) node).stack.indexOf(object);
                  ((FrameNode) node)
                      .stack.set(
                          index,
                          ((String) object)
                              .replace(
                                  formatClassName((String) object),
                                  classMapping2.getClassData(1).getName()));
                }
              }
            }
          }

          if (((FrameNode) node).local != null) {
            for (Object object : ((FrameNode) node).local) {
              if (object instanceof String) {
                ClassMapping classMapping2 =
                    mappings.getClassMapping(formatClassName((String) object), 0);

                if (classMapping2 != null) {
                  int index = ((FrameNode) node).local.indexOf(object);
                  ((FrameNode) node)
                      .local.set(
                          index,
                          ((String) object)
                              .replace(
                                  formatClassName((String) object),
                                  classMapping2.getClassData(1).getName()));
                }
              }
            }
          }
        }
        if (node instanceof MultiANewArrayInsnNode) {
          ClassMapping classMapping2 =
              mappings.getClassMapping(formatClassName(((MultiANewArrayInsnNode) node).desc), 0);

          if (classMapping2 != null) {
            ((MultiANewArrayInsnNode) node).desc =
                ((MultiANewArrayInsnNode) node)
                    .desc.replace(
                        formatClassName(((MultiANewArrayInsnNode) node).desc),
                        classMapping2.getClassData(1).getName());
          }
        }
        if (node instanceof InvokeDynamicInsnNode) {
          ArrayList<Object> bsmArgs =
              new ArrayList<>(Arrays.asList(((InvokeDynamicInsnNode) node).bsmArgs));

          int i = 0;
          for (Object object : bsmArgs) {
            if (object instanceof Handle) {
              Handle handle = (Handle) object;

              String owner = handle.getOwner();
              String desc = handle.getDesc();

              ClassMapping classMapping2 = mappings.getClassMapping(handle.getOwner(), 0);

              if (classMapping2 != null) {
                owner = classMapping2.getClassData(1).getName();
              }

              for (Type type : Type.getType(desc).getArgumentTypes()) {
                ClassMapping classMapping3 =
                    mappings.getClassMapping(formatClassName(type.getClassName()), 0);

                if (classMapping3 != null) {
                  desc =
                      desc.replace(
                          formatClassName(type.getClassName()),
                          classMapping3.getClassData(1).getName());
                }
              }

              Handle handle1 = new Handle(handle.getTag(), owner, handle.getName(), desc);
              ((InvokeDynamicInsnNode) node).bsmArgs[bsmArgs.indexOf(handle)] = handle1;
            } else if (object instanceof Type) {
              for (Type type : ((Type) object).getArgumentTypes()) {
                ClassMapping classMapping2 =
                    mappings.getClassMapping(formatClassName(type.getClassName()), 0);

                if (classMapping2 != null) {
                  ((InvokeDynamicInsnNode) node).bsmArgs[i] =
                      Type.getType(
                          object
                              .toString()
                              .replace(
                                  formatClassName(type.getClassName()),
                                  classMapping2.getClassData(1).getName()));
                }
              }
            }

            i++;
          }
        }
      }

      for (TryCatchBlockNode tryCatchBlockNode :
          (List<TryCatchBlockNode>) methodNode.tryCatchBlocks) {

        ClassMapping classMapping2 = mappings.getClassMapping(tryCatchBlockNode.type, 0);

        if (classMapping2 != null) {
          tryCatchBlockNode.type = classMapping2.getClassData(1).getName();
        }
      }

      Type type = Type.getType(methodNode.desc);

      Map<String, String> classMappings = new HashMap<>();

      for (Type type1 : type.getArgumentTypes()) {
        ClassMapping classMapping2 =
            mappings.getClassMapping(formatClassName(type1.getClassName()), 0);

        if (classMapping2 != null) {
          classMappings.put(
              formatClassName(type1.getClassName()), classMapping2.getClassData(1).getName());
        }
      }

      Type type1 = type.getReturnType();

      ClassMapping classMapping2 =
          mappings.getClassMapping(formatClassName(type1.getClassName()), 0);

      if (classMapping2 != null) {
        classMappings.put(
            formatClassName(type1.getClassName()), classMapping2.getClassData(1).getName());
      }

      if (classMapping != null) {
        if (classMapping.getClassData(0).getMethodData(methodNode.name, methodNode.desc) != null) {
          methodNode.name =
              classMapping
                  .getClassData(1)
                  .getMethodDatas()
                  .get(
                      classMapping
                          .getClassData(0)
                          .getMethodDatas()
                          .indexOf(
                              classMapping
                                  .getClassData(0)
                                  .getMethodData(methodNode.name, methodNode.desc)))
                  .getName();
        }
      }

      for (String string : classMappings.keySet()) {
        methodNode.desc =
            methodNode.desc.replace("L" + string + ";", "L" + classMappings.get(string) + ";");
      }

      if (methodNode.localVariables != null) {
        methodNode.localVariables.clear();
      }

      methodNode.signature = formatSignature(methodNode.signature, mappings);
      if ((methodNode.access & Opcodes.ACC_PRIVATE) != 0) {
        methodNode.access -= 1;
      }
      if ((methodNode.access & Opcodes.ACC_PROTECTED) != 0) {
        methodNode.access -= 3;
      }
    }

    for (FieldNode fieldNode : (List<FieldNode>) classNode.fields) {

      if (classMapping != null) {
        if (classMapping.getClassData(0).getFieldData(fieldNode.name) != null) {
          fieldNode.name =
              classMapping
                  .getClassData(1)
                  .getFieldDatas()
                  .get(
                      classMapping
                          .getClassData(0)
                          .getFieldDatas()
                          .indexOf(classMapping.getClassData(0).getFieldData(fieldNode.name)))
                  .getName();
        }
      }

      Type type = Type.getType(fieldNode.desc);

      ClassMapping classMapping2 =
          mappings.getClassMapping(formatClassName(type.getClassName()), 0);

      if (classMapping2 != null) {
        fieldNode.desc =
            fieldNode.desc.replace(
                formatClassName(type.getClassName()), classMapping2.getClassData(1).getName());
      }

      fieldNode.signature = formatSignature(fieldNode.signature, mappings);
      if ((fieldNode.access & Opcodes.ACC_PRIVATE) != 0) {
        fieldNode.access -= 1;
      }
      if ((fieldNode.access & Opcodes.ACC_PROTECTED) != 0) {
        fieldNode.access -= 3;
      }
    }
  }

  private static String formatClassName(String string) {
    String result = string.replace(".", "/").replace("[", "").replace("]", "").replace(";", "");
    if (result.startsWith("L")) {
      result = result.substring(1);
    }
    return result;
  }

  private static String formatSignature(String signature, Mappings mappings) {
    if (signature != null) {

      boolean looking = false;
      StringBuilder string = new StringBuilder();
      String prevString = "";

      for (int i = 0; i < signature.length(); i++) {
        char character = signature.charAt(i);

        if (looking) {
          string.append(character);
        }

        switch (character) {
          case 'L':
            looking = true;
            break;
          case ';':
          case '<':
            looking = false;

            if (string.length() > 0) {
              ClassMapping classMapping3 =
                  mappings.getClassMapping(string.substring(0, string.length() - 1), 0);

              if (classMapping3 != null) {
                signature =
                    signature.replace(
                        string.substring(0, string.length() - 1),
                        classMapping3.getClassData(1).getName());
              }

              if (prevString.length() > 0) {
                classMapping3 =
                    mappings.getClassMapping(prevString.substring(0, prevString.length() - 1), 0);

                if (classMapping3 != null) {
                  signature =
                      signature.replace(
                          prevString.substring(0, prevString.length() - 1),
                          classMapping3.getClassData(1).getName());
                }
              }
            }
            prevString = string.toString();
            string = new StringBuilder();
            break;
        }
      }
    }

    return signature;
  }

  private <T, U> IInjectionType<T, U> getInjectionType(Class<T> t) {
    return (IInjectionType<T, U>) injectionTypes.get(t);
  }
}

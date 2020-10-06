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

package org.sorus.client.obfuscation;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import org.objectweb.asm.Type;
import org.sorus.client.obfuscation.mappings.ClassMapping;
import org.sorus.client.obfuscation.mappings.Mappings;
import org.sorus.client.startup.SorusStartup;

/** Helps handle the obfuscation of the game. */
public class ObfuscationManager {

  private static final boolean dev = SorusStartup.DEV;
  private static final Mappings mappings =
      Mappings.from(
          new Scanner(
              Objects.requireNonNull(
                  ObfuscationManager.class.getClassLoader().getResourceAsStream("mappings.txt"))));

  public static Mappings externalMappings;

  static {
    String mappings = SorusStartup.getLaunchArgs().get("mappings");
    if (mappings != null) {
      externalMappings = Mappings.from(new File("sorus/mappings/" + mappings + ".txt"));
    }
  }

  /**
   * Returns the deobfuscated or obfuscated class name, depending on if its a dev environment.
   *
   * @param className the input class name
   * @return the actual class name
   */
  public static String getClassName(String className) {
    String rawName = ObfuscationManager.getClassNameRaw(className);
    if (externalMappings == null) {
      return rawName;
    }
    ClassMapping classMapping = externalMappings.getClassMapping(rawName, 0);
    if (classMapping != null) {
      return classMapping.getClassData(1).getName();
    }
    return rawName;
  }

  private static String getClassNameRaw(String className) {
    if (dev) {
      return className;
    }
    ClassMapping classMapping = mappings.getClassMapping(className, 1);
    if (classMapping != null) {
      return classMapping.getClassData(0).getName();
    }
    return className;
  }

  /**
   * Returns the deobfuscated or obfuscated method name, depending on if its a dev environment.
   *
   * @param className the input class name
   * @param methodName the input method name
   * @param methodDesc the description of the input method
   * @return the actual method name
   */
  public static String getMethodName(String className, String methodName, String methodDesc) {
    String result = methodName;
    if (!dev) {
      ClassMapping classMapping = mappings.getClassMapping(className, 1);
      if (classMapping != null) {
        result =
            mappings
                .getClassMapping(className, 1)
                .getClassData(0)
                .getMethodDatas()
                .get(
                    mappings
                        .getClassMapping(className, 1)
                        .getClassData(1)
                        .getMethodDatas()
                        .indexOf(
                            mappings
                                .getClassMapping(className, 1)
                                .getClassData(1)
                                .getMethodData(methodName, methodDesc)))
                .getName();
      }
    }
    String newClassName = ObfuscationManager.getClassNameRaw(className);
    if (externalMappings != null) {
      ClassMapping classMapping = externalMappings.getClassMapping(newClassName, 0);
      if (classMapping != null) {
        result =
            externalMappings
                .getClassMapping(newClassName, 0)
                .getClassData(1)
                .getMethodDatas()
                .get(
                    externalMappings
                        .getClassMapping(newClassName, 0)
                        .getClassData(0)
                        .getMethodDatas()
                        .indexOf(
                            externalMappings
                                .getClassMapping(newClassName, 0)
                                .getClassData(0)
                                .getMethodData(result, ObfuscationManager.getDescRaw(methodDesc))))
                .getName();
      }
    }
    return result;
  }

  public static String getFieldName(String className, String fieldName) {
    String result;
    if (dev) {
      result = fieldName;
    } else {
      result =
          mappings
              .getClassMapping(className, 1)
              .getClassData(0)
              .getFieldDatas()
              .get(
                  mappings
                      .getClassMapping(className, 1)
                      .getClassData(1)
                      .getFieldDatas()
                      .indexOf(
                          mappings
                              .getClassMapping(className, 1)
                              .getClassData(1)
                              .getFieldData(fieldName)))
              .getName();
    }
    String newClassName = ObfuscationManager.getClassNameRaw(className);
    if (externalMappings == null) {
      return result;
    }
    return externalMappings
        .getClassMapping(newClassName, 0)
        .getClassData(1)
        .getFieldDatas()
        .get(
            externalMappings
                .getClassMapping(newClassName, 0)
                .getClassData(0)
                .getFieldDatas()
                .indexOf(
                    externalMappings
                        .getClassMapping(newClassName, 0)
                        .getClassData(0)
                        .getFieldData(result)))
        .getName();
  }

  /**
   * Returns the deobfuscated or obfuscated method desc, depending on if its a dev environment.
   *
   * @param desc the input description
   * @return the actual description
   */
  public static String getDesc(String desc) {
    String rawDesc = ObfuscationManager.getDescRaw(desc);
    if (externalMappings == null) {
      return rawDesc;
    }
    Type type = Type.getType(rawDesc);
    for (Type type1 : type.getArgumentTypes()) {
      String result = type1.getClassName().replace(".", "/");
      ClassMapping classMapping =
          externalMappings.getClassMapping(type1.getClassName().replace(".", "/"), 0);
      if (classMapping != null) {
        result = classMapping.getClassData(1).getName();
      }
      rawDesc = rawDesc.replace(type1.getClassName().replace(".", "/"), result);
    }
    String result = type.getReturnType().getClassName().replace(".", "/");
    ClassMapping classMapping =
        externalMappings.getClassMapping(type.getReturnType().getClassName().replace(".", "/"), 0);
    if (classMapping != null) {
      result = classMapping.getClassData(1).getName();
    }
    rawDesc = rawDesc.replace(type.getReturnType().getClassName().replace(".", "/"), result);
    return rawDesc;
  }

  private static String getDescRaw(String desc) {
    if (dev) {
      return desc;
    }
    Type type = Type.getType(desc);
    for (Type type1 : type.getArgumentTypes()) {
      String result = type1.getClassName().replace(".", "/");
      ClassMapping classMapping =
          mappings.getClassMapping(type1.getClassName().replace(".", "/"), 1);
      if (classMapping != null) {
        result = classMapping.getClassData(0).getName();
      }
      desc = desc.replace(type1.getClassName().replace(".", "/"), result);
    }
    String result = type.getReturnType().getClassName().replace(".", "/");
    ClassMapping classMapping =
        mappings.getClassMapping(type.getReturnType().getClassName().replace(".", "/"), 1);
    if (classMapping != null) {
      result = classMapping.getClassData(0).getName();
    }
    desc = desc.replace(type.getReturnType().getClassName().replace(".", "/"), result);
    return desc;
  }
}

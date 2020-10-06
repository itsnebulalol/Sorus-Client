package org.sorus.client.obfuscation.mappings;

import java.util.ArrayList;
import java.util.List;

public class ClassData {

  ClassMapping classMapping;

  private String name;
  private String superNames;
  private int index;
  private final List<MethodData> methodData = new ArrayList<>();
  private final List<FieldData> fieldData = new ArrayList<>();
  private final List<ClassData> superData = new ArrayList<>();

  public void setName(String name, String superNames) {
    this.name = name;
    this.superNames = superNames;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void addMethodData(MethodData methodData) {
    this.methodData.add(methodData);
  }

  public void addFieldData(FieldData fieldData) {
    this.fieldData.add(fieldData);
  }

  public List<MethodData> getMethodDatas() {
    List<MethodData> methodData = new ArrayList<>(this.methodData);
    for (ClassData classData : this.superData) {
      methodData.addAll(classData.getMethodDatas());
    }
    return methodData;
  }

  public List<FieldData> getFieldDatas() {
    List<FieldData> fieldData = new ArrayList<>(this.fieldData);
    for (ClassData classData : this.superData) {
      fieldData.addAll(classData.getFieldDatas());
    }
    return fieldData;
  }

  public MethodData getMethodData(String name, String desc) {
    for (MethodData methodData : this.getMethodDatas()) {
      if (methodData.getName().equals(name) && methodData.getDesc().equals(desc)) {
        return methodData;
      }
    }

    return null;
  }

  public FieldData getFieldData(String name) {
    for (FieldData fieldData : this.getFieldDatas()) {
      if (fieldData.getName().equals(name)) {
        return fieldData;
      }
    }

    return null;
  }

  public String getName() {
    return name;
  }

  public void setClassMapping(ClassMapping classMapping) {
    this.classMapping = classMapping;
  }

  public ClassData getAlternateData(int index) {
    return this.classMapping.getClassData(index);
  }

  public void initializeSupers(Mappings mappings) {
    if (this.superNames.length() == 0) {
      return;
    }
    for (String string : superNames.split(";")) {
      if (!string.isEmpty()) {
        this.superData.add(mappings.getClassMapping(string, index).getClassData(index));
      }
    }
  }
}

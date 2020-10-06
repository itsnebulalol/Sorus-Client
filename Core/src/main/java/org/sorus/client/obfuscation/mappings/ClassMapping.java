package org.sorus.client.obfuscation.mappings;

import java.util.ArrayList;
import java.util.List;

public class ClassMapping {

  private final List<ClassData> classData = new ArrayList<>();

  public void add(ClassData classData) {
    this.classData.add(classData);
    classData.setClassMapping(this);
    classData.setIndex(this.classData.size() - 1);
  }

  public List<ClassData> getClassData() {
    return classData;
  }

  public ClassData getClassData(int index) {
    return classData.get(index);
  }
}

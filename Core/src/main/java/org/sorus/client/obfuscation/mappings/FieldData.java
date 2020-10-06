package org.sorus.client.obfuscation.mappings;

public class FieldData {

  private ClassData classData;

  private String name;

  public void setClassData(ClassData classData) {
    this.classData = classData;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public FieldData getAlternateData(int index) {
    return this.classData
        .classMapping
        .getClassData(index)
        .getFieldDatas()
        .get(this.classData.getFieldDatas().indexOf(this));
  }
}

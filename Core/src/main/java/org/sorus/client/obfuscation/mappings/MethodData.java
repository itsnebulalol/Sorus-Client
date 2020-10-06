package org.sorus.client.obfuscation.mappings;

public class MethodData {

  private ClassData classData;

  private String name;
  private String desc;

  public void setClassData(ClassData classData) {
    this.classData = classData;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public MethodData getAlternateData(int index) {
    return this.classData
        .classMapping
        .getClassData(index)
        .getMethodDatas()
        .get(this.classData.getMethodDatas().indexOf(this));
  }
}

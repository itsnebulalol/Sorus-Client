package org.sorus.client.obfuscation.mappings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mappings {

  private final List<ClassMapping> classMappings = new ArrayList<>();

  public static Mappings from(File file) {
    try {
      return Mappings.from(new Scanner(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Mappings from(Scanner scanner) {

    Mappings mappings = new Mappings();

    List<ClassData> classDataList = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (!line.contains("     ")) {
        classDataList.clear();
        for (String string : line.split(" ")) {
          ClassData classData = new ClassData();
          int semicolonIndex = string.indexOf(";");
          if (semicolonIndex == -1) {
            semicolonIndex = string.length();
          }
          classData.setName(string.substring(0, semicolonIndex), string.substring(semicolonIndex));
          classDataList.add(classData);
        }
        ClassMapping classMapping = new ClassMapping();
        for (ClassData classData : classDataList) {
          classMapping.add(classData);
        }
        mappings.addClassMapping(classMapping);
      } else if (line.contains("(")) {
        String substring = line.substring(5);
        int i = 0;
        for (String string : substring.split(" ")) {
          MethodData methodData = new MethodData();
          int descIndex = string.indexOf("(");
          methodData.setName(string.substring(0, descIndex));
          methodData.setDesc(string.substring(descIndex));
          methodData.setClassData(classDataList.get(i));
          classDataList.get(i).addMethodData(methodData);
          i++;
        }
      } else {
        String substring = line.substring(5);
        int i = 0;
        for (String string : substring.split(" ")) {
          FieldData fieldData = new FieldData();
          fieldData.setName(string);
          fieldData.setClassData(classDataList.get(i));
          classDataList.get(i).addFieldData(fieldData);
          i++;
        }
      }
    }

    for (ClassMapping classMapping : mappings.classMappings) {
      for (ClassData classData : classMapping.getClassData()) {
        classData.initializeSupers(mappings);
      }
    }

    return mappings;
  }

  public void addClassMapping(ClassMapping classMapping) {
    this.classMappings.add(classMapping);
  }

  public ClassMapping getClassMapping(String name, int index) {
    for (ClassMapping classMapping : this.classMappings) {
      if (classMapping.getClassData().get(index).getName().equals(name)) {
        return classMapping;
      }
    }

    return null;
  }
}

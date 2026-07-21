package entity;

import com.opencsv.bean.CsvBindByName;

public class Department {
  @CsvBindByName(column = "id")
  private int id;

  @CsvBindByName(column = "name")
  private String name;

  public Department(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }
}

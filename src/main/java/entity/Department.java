package entity;

import com.opencsv.bean.CsvBindByName;

import lib.Identifiable;

public class Department implements Identifiable {
  @CsvBindByName(column = "id")
  private int id;

  @CsvBindByName(column = "name")
  private String name;

  public Department() {}

  public Department(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setID(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getID() {
    return id;
  }
}

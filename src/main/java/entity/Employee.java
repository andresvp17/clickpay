package entity;

import com.opencsv.bean.CsvBindByName;

public class Employee {
  @CsvBindByName(column = "id")
  private int id;

  @CsvBindByName(column = "age")
  private int age;

  @CsvBindByName(column = "name")
  private String name;

  @CsvBindByName(column = "lastname")
  private String lastname;

  @CsvBindByName(column = "salary")
  private double salary;

  @CsvBindByName(column = "department_id")
  private int departmentID;

  public Employee(int id, int age, String name, String lastname, double salary, int departmentID) {
    this.id = id;
    this.age = age;
    this.name = name;
    this.lastname = lastname;
    this.salary = salary;
    this.departmentID = departmentID;
  }

  public void setDepartmentID(int departmentID) {
    this.departmentID = departmentID;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

  public int getId() {
    return id;
  }

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }

  public String getLastname() {
    return lastname;
  }

  public double getSalary() {
    return salary;
  }

  public int getDepartmentID() {
    return departmentID;
  }
}

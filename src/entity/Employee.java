package entity;

public class Employee {
  private int id;
  private int age;
  private String name;
  private String lastname;
  private double salary;
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

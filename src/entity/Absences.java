package entity;

import java.time.LocalDate;

public class Absences {
  private int id;
  private int employeeID;
  private LocalDate date;
  private String reason;
  private boolean isUnpaid;

  public Absences(int id, int employeeID, LocalDate date, String reason, boolean isUnpaid) {
    this.id = id;
    this.employeeID = employeeID;
    this.date = date;
    this.reason = reason;
    this.isUnpaid = isUnpaid;
  }

  public int getId() {
    return id;
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public LocalDate getDate() {
    return date;
  }

  public String getReason() {
    return reason;
  }

  public boolean getIsUnpaid() {
    return isUnpaid;
  }
}

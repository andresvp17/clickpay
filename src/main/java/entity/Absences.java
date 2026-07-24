package entity;

import java.time.LocalDate;
import com.opencsv.bean.CsvBindByName;

import lib.Identifiable;

public class Absences implements Identifiable {
  @CsvBindByName(column = "id")
  private int id;

  @CsvBindByName(column = "employee_id")
  private int employeeID;

  @CsvBindByName(column = "date")
  private LocalDate date;

  @CsvBindByName(column = "reason")
  private String reason;

  @CsvBindByName(column = "is_unpaid")
  private boolean isUnpaid;

  public Absences() {
  }

  public Absences(int id, int employeeID, LocalDate date, String reason, boolean isUnpaid) {
    this.id = id;
    this.employeeID = employeeID;
    this.date = date;
    this.reason = reason;
    this.isUnpaid = isUnpaid;
  }

  public int getID() {
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

  public void setId(int id) {
    this.id = id;
  }

  public void setEmployeeID(int employeeID) {
    this.employeeID = employeeID;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public void setUnpaid(boolean isUnpaid) {
    this.isUnpaid = isUnpaid;
  }
}

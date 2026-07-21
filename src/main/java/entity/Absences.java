package entity;

import java.time.LocalDate;
import com.opencsv.bean.CsvBindByName;

public class Absences {
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

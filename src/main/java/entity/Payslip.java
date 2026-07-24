package entity;

import com.opencsv.bean.CsvBindByName;

import lib.Identifiable;

public class Payslip implements Identifiable {
  @CsvBindByName(column = "id")
  private int id;

  @CsvBindByName(column = "employee_id")
  private int employeeID;

  @CsvBindByName(column = "period")
  private String period;

  @CsvBindByName(column = "gross_pay")
  private double grossPay;

  @CsvBindByName(column = "deductions")
  private double deductions;

  @CsvBindByName(column = "net_pay")
  private double netPay;

  public Payslip() {}

  public Payslip(int id, int employeeID, String period, double grossPay, double deductions, double netPay) {
    this.id = id;
    this.employeeID = employeeID;
    this.period = period;
    this.grossPay = grossPay;
    this.deductions = deductions;
    this.netPay = netPay;
  }

  public int getID() {
    return id;
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public double getDeductions() {
    return deductions;
  }

  public String getPeriod() {
    return period;
  }

  public double getGrossPay() {
    return grossPay;
  }

  public double getNetPay() {
    return netPay;
  }

  public void setDeductions(double deductions) {
    this.deductions = deductions;
  }

  public void setGrossPay(double grossPay) {
    this.grossPay = grossPay;
  }

  public void setNetPay(double netPay) {
    this.netPay = netPay;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public void setID(int id) {
    this.id = id;
  }

  public void setEmployeeID(int id) {
    this.employeeID = id;
  }
}

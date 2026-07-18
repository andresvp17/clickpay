package entity;

public class Payslip {
  private int id;
  private int employeeID;
  private String period;
  private double grossPay;
  private double deductions;
  private double netPay;

  public Payslip(int id, int employeeID, String period, double grossPay, double deductions, double netPay) {
    this.id = id;
    this.employeeID = employeeID;
    this.period = period;
    this.grossPay = grossPay;
    this.deductions = deductions;
    this.netPay = netPay;
  }

  public int getId() {
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
}

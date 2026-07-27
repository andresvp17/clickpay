package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import entity.Employee;
import entity.Payslip;
import lib.Utility;
import repository.EmployeeRepo;
import repository.PayslipRepo;

public class PayslipService {
    PayslipRepo payslipRepo;
    EmployeeRepo employeeRepo;

    public PayslipService(PayslipRepo payslipRepo, EmployeeRepo employeeRepo) {
        this.payslipRepo = payslipRepo;
        this.employeeRepo = employeeRepo;
    }

    public Optional<Payslip> getPayslipByID(int id) {
        return payslipRepo.findByID(id);
    }

    public List<Payslip> getAllPayslip() {
        List<Payslip> payslips = payslipRepo.findAll();

        if (payslips.isEmpty()) {
            return List.of();
        }

        return payslips;
    }

    public void createPayslip(int employeeID, String period, double grossPay, double deductions, double netPay) {
        if (employeeID <= 0) {
            throw new IllegalArgumentException("No se puede colocar una ID de empleado menor a 1");
        }

        if (period.isEmpty()) {
            throw new IllegalArgumentException("No se puede dejar sin colocar el periodo de pago");
        }

        if (grossPay <= 0 || netPay <= 0) {
            throw new IllegalArgumentException("El pago neto y el pago bruto no pueden ser 0!");
        }

        if (deductions < 0) {
            throw new IllegalArgumentException("Las deducciones no pueden ser menores a 0");
        }

        int maxID = Utility.getMaxID(payslipRepo.findAll()) + 1;

        Payslip payslip = new Payslip(maxID, employeeID, period, grossPay, deductions, netPay);
        payslipRepo.save(payslip);
    }

    public void updatePayslip(int id, int employeeID, String period, double grossPay, double deductions,
            double netPay) {
        Payslip payslipToUpdate = payslipRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el recibo con el ID " + id));

        if (period != null) {
            payslipToUpdate.setPeriod(period);
        }

        if (grossPay > 0) {
            payslipToUpdate.setGrossPay(grossPay);
        }

        if (netPay > 0) {
            payslipToUpdate.setNetPay(netPay);
        }

        if (deductions >= 0) {
            payslipToUpdate.setDeductions(deductions);
        }

        if (employeeID > 0) {
            payslipToUpdate.setEmployeeID(employeeID);
        }

        payslipRepo.update(payslipToUpdate);
    }

    public void generate(int id) throws IOException {
        Payslip payslip = payslipRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El recibo con la ID " + id + " no se encontró"));

        Employee employee = employeeRepo.findByID(payslip.getEmployeeID())
                .orElseThrow(() -> new NoSuchElementException(
                        "El empleado con la ID " + payslip.getEmployeeID() + " no se encontró"));

        String name = employee.getName() + ' ' + employee.getLastname();
        Path pdfDir = Path.of("pdfs");
        Files.createDirectories(pdfDir);
        Path pathname = pdfDir.resolve("payslip_" + payslip.getPeriod() + "_" + name.replace(" ", "_") + ".pdf");
        payslipRepo.generatePayslip(payslip, name, pathname);
    }

    public void deletePayslip(int id) {
        payslipRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El recibo con la ID " + id + " no se encontró"));

        payslipRepo.delete(id);
    }
}

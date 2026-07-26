package repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import entity.Payslip;
import lib.CsvRepo;

public class PayslipRepo extends CsvRepo<Payslip, Integer> {

    public PayslipRepo(Path csvPathFile) {
        super(csvPathFile, Payslip.class);
    }

    public void generatePayslip(Payslip payslip, String employeeName, Path outputPath) throws IOException {
        String html;
        byte[] logo = getClass().getResourceAsStream("/lib/clickpay.png").readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(logo);
        String uri = "data:image/png;base64," + base64;

        try (InputStream inputStream = getClass().getResourceAsStream("/lib/payslip.html")) {
            if (inputStream == null) {
                throw new IOException("Plantalla no encontrada en /lib/payslip.html");
            }
            html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        html = html.replace("${payPeriod}", payslip.getPeriod())
                .replace("${companyLogo}", uri)
                .replace("${employeeName}", employeeName)
                .replace("${employeeId}", String.valueOf(payslip.getEmployeeID()))
                .replace("${baseSalary}", String.format("%.2f", payslip.getGrossPay()))
                .replace("${taxDeduction}", String.format("%.2f", payslip.getDeductions()))
                .replace("${netPay}", String.format("%.2f", payslip.getNetPay()));

        try (FileOutputStream os = new FileOutputStream(outputPath.toFile())) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }
    }

    @Override
    protected Integer getID(Payslip entity) {
        return entity.getID();
    }
}

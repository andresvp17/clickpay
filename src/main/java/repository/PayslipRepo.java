package repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
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
        String uri = "";
        try (InputStream logoStream = getClass().getResourceAsStream("/lib/clickpay.png")) {
            if (logoStream != null) {
                byte[] logo = logoStream.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(logo);
                uri = "data:image/png;base64," + base64;
            } else {
                // resource not found; leave uri empty so the template can handle missing logo
                uri = "";
            }
        }

        // Try to load template from classpath first, fall back to source/resources
        // folder during development
        InputStream inputStream = getClass().getResourceAsStream("/lib/payslip.html");
        if (inputStream != null) {
            try (InputStream is = inputStream) {
                html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } else {
            // fallback locations developers may have used
            Path p1 = Path.of("src/main/resources/lib/payslip.html");
            Path p2 = Path.of("src/main/java/lib/payslip.html");
            Path chosen = null;
            if (Files.exists(p1)) {
                chosen = p1;
            } else if (Files.exists(p2)) {
                chosen = p2;
            }

            if (chosen == null) {
                throw new IOException("Plantilla no encontrada en /lib/payslip.html nor in src folders");
            }

            html = Files.readString(chosen, StandardCharsets.UTF_8);
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

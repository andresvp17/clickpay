package repository;

import java.nio.file.Path;
import entity.Payslip;
import lib.CsvRepo;

public class PayslipRepo extends CsvRepo<Payslip, Integer> {

    public PayslipRepo(Path csvPathFile) {
        super(csvPathFile, Payslip.class);
    }

    @Override
    protected Integer getID(Payslip entity) {
        return entity.getID();
    }
}

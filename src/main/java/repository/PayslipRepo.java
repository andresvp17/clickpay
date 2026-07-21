package repository;

import java.nio.file.Paths;
import java.util.List;
import entity.Payslip;
import lib.CsvRepo;

interface IPayslipRepo {
    Payslip findByID(int id);

    List<Payslip> findAll();

    void save(Payslip payslip);

    void update(Payslip payslip);

    void delete(int id);
}

public class PayslipRepo extends CsvRepo<Payslip, Integer> implements IPayslipRepo {
    private List<Payslip> payslips;

    public PayslipRepo(String csvPathFile) {
        super(Paths.get(csvPathFile), Payslip.class);
    }

    public Payslip findByID(int id) {
        for (Payslip payslip : payslips) {
            if (payslip.getId() == id) {
                return payslip;
            }
        }

        return null;
    }

    @Override
    protected Integer getID(Payslip entity) {
        return entity.getId();
    }

    public List<Payslip> findAll() {
        return payslips;
    }

    public void save(Payslip payslip) {
        payslips.add(payslip);
    }

    public void delete(int id) {
        payslips.removeIf(payslip -> payslip.getId() == id);
    }

    public void update(Payslip payslip) {
        for (int i = 0; i < payslips.size(); i++) {
            if (payslips.get(i).getId() == payslip.getId()) {
                payslips.set(i, payslip);
                break;
            }
        }
    }
}
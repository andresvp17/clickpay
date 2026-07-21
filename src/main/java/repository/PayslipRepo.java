package repository;

import java.util.List;
import entity.Payslip;

interface IPayslipRepo {
    Payslip findByID(int id);

    List<Payslip> findAll();

    void save(Payslip payslip);

    void delete(int id);

    void update(Payslip payslip);
}

public class PayslipRepo implements IPayslipRepo {
    private List<Payslip> payslips;

    public Payslip findByID(int id) {
        for (Payslip payslip : payslips) {
            if (payslip.getId() == id) {
                return payslip;
            }
        }

        return null;
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
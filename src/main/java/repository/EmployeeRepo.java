package repository;

import java.nio.file.Path;
import entity.Employee;
import lib.CsvRepo;

public class EmployeeRepo extends CsvRepo<Employee, Integer> {

    public EmployeeRepo(Path csvPathFile) {
        super(csvPathFile, Employee.class);
    }

    @Override
    protected Integer getID(Employee entity) {
        return entity.getID();
    }
}

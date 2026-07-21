package repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import entity.Employee;
import lib.CsvRepo;

interface IEmployeeRepository {
    Employee findByID(int id);

    List<Employee> findAll();

    void save(Employee employee);

    void update(Employee employee);

    void delete(int id);
}

public class EmployeeRepo extends CsvRepo<Employee, Integer> implements IEmployeeRepository {
    private List<Employee> employees;

    public EmployeeRepo(String csvPathFile) {
        super(Paths.get(csvPathFile), Employee.class);
    }

    public Employee findByID(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
    }

    @Override
    protected Integer getID(Employee entity) {
        return entity.getId();
    }

    public List<Employee> findAll() {
        return employees;
    }

    public void save(Employee employee) {
        employees.add(employee);
    }

    public void delete(int id) {
        employees.removeIf(employee -> employee.getId() == id);
    }

    public void update(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee);
                break;
            }
        }
    }
}

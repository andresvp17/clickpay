package repository;

import java.util.List;
import entity.Employee;

interface IEmployeeRepository {
    Employee findByID(int id);

    List<Employee> findAll();

    void save(Employee employee);

    void delete(int id);

    void update(Employee employee);
}

public class EmployeeRepo implements IEmployeeRepository {
    private List<Employee> employees;

    public EmployeeRepo(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee findByID(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
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

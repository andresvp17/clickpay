package services;

import repository.EmployeeRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import entity.Employee;
import lib.Utility;

public class EmployeeService {
    EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public void createEmployee(String name, String lastname, int age, double salary, int departmentID) {
        if (age < 18) {
            throw new IllegalArgumentException("El empleado debe ser mayor a 18!");
        }

        if (name.isEmpty() || lastname.isEmpty()) {
            throw new IllegalArgumentException("Ni el nombre ni el apellido pueden estar vacios");
        }

        if (salary <= 0) {
            throw new IllegalArgumentException("El salario del cliente debe ser mayor a 0!");
        }

        if (departmentID <= 0) {
            throw new IllegalArgumentException("La ID del departamento no es válida");
        }

        int maxID = Utility.getMaxID(employeeRepo.findAll());

        Employee employee = new Employee(maxID + 1, age, name, lastname, salary, departmentID);
        employeeRepo.save(employee);
    }

    public Optional<Employee> getEmployeeByID(int id) {
        return employeeRepo.findByID(id);
    }

    public List<Employee> findAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();

        if (employees.isEmpty()) {
            return List.of();
        }

        return employees;
    }

    public void updateEmployee(int id, String name, String lastname, int age, double salary, int departmentID) {
        Employee employeeToUpdate = employeeRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El empleado con ID " + id + " no fue encontrado"));

        if (age < 18) {
            throw new IllegalArgumentException("El empleado debe ser mayor a 18!");
        }

        if (name.isEmpty() || lastname.isEmpty()) {
            throw new IllegalArgumentException("Ni el nombre ni el apellido pueden estar vacios");
        }

        if (salary <= 0) {
            throw new IllegalArgumentException("El salario debe ser mayor a 0!");
        }

        if (departmentID <= 0) {
            throw new IllegalArgumentException("La ID del departamento no es válida");
        }

        employeeToUpdate.setName(name);
        employeeToUpdate.setLastname(lastname);
        employeeToUpdate.setAge(age);
        employeeToUpdate.setSalary(salary);
        employeeToUpdate.setDepartmentID(departmentID);

        employeeRepo.update(employeeToUpdate);
    }

    public void deleteEmployee(int id) {
        employeeRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El empleado con ID " + id + " no fue encontrado"));

        employeeRepo.delete(id);
    }
}

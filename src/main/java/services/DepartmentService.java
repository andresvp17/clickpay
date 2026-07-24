package services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import entity.Department;
import lib.Utility;
import repository.DepartmentRepo;

public class DepartmentService {
    DepartmentRepo departmentRepo;

    public DepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    public Optional<Department> getDepartmentByID(int id) {
        return departmentRepo.findByID(id);
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = departmentRepo.findAll();

        if (departments.isEmpty()) {
            return List.of();
        }

        return departments;
    }

    public void createDepartment(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del departamento no puede estar vacío");
        }

        int maxID = Utility.getMaxID(departmentRepo.findAll());

        Department department = new Department(maxID + 1, name);
        departmentRepo.save(department);
    }

    public void updateDepartment(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("La ID no puede ser un valor menor a 1");
        }

        Department department = departmentRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El departamento con la ID " + id + " no se encontró"));

        if (!name.isEmpty()) {
            department.setName(name);
        }

        departmentRepo.update(department);
    }

    public void deleteDepartment(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("La ID no puede ser un valor menor a 1");
        }

        departmentRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("El departamento con la ID " + id + " no se encontró"));

        departmentRepo.delete(id);
    }
}

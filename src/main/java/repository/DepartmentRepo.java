package repository;

import java.nio.file.Paths;
import java.util.List;

import entity.Department;
import lib.CsvRepo;

interface IDepartmentRepo {
    Department findByID(int id);

    List<Department> findAll();

    void save(Department department);

    void update(Department department);

    void delete(int id);
}

public class DepartmentRepo extends CsvRepo<Department, Integer> implements IDepartmentRepo {
    List<Department> departments;

    public DepartmentRepo(String csvFilePath) {
        super(Paths.get(csvFilePath), Department.class);
    }

    public Department findByID(int id) {
        for (Department department : departments) {
            if (department.getId() == id) {
                return department;
            }
        }

        return null;
    }

    public List<Department> findAll() {
        return departments;
    }

    public void save(Department department) {
        departments.add(department);
    }

    public void update(Department departmentToUpdate) {
        for (int i = 0; i < departments.size(); i++) {
            if (departmentToUpdate.getId() == departments.get(i).getId()) {
                departments.set(i, departmentToUpdate);
            }
        }
    }

    @Override
    protected Integer getID(Department entity) {
        return entity.getId();
    }

    public void delete(int id) {
        departments.removeIf(department -> department.getId() == id);
    }
}
package repository;

import java.nio.file.Path;

import entity.Department;
import lib.CsvRepo;

public class DepartmentRepo extends CsvRepo<Department, Integer> {

    public DepartmentRepo(Path csvFilePath) {
        super(csvFilePath, Department.class);
    }

    @Override
    protected Integer getID(Department entity) {
        return entity.getID();
    }
}

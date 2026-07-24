package repository;

import java.nio.file.Path;
import java.util.List;

import entity.Absences;
import lib.CsvRepo;

public class AbsencesRepo extends CsvRepo<Absences, Integer> {

    public AbsencesRepo(Path csvToFile) {
        super(csvToFile, Absences.class);
    }

    @Override
    protected Integer getID(Absences entity) {
        return entity.getID();
    }

    public List<Absences> findByEmployeeID(int employeeID) {
        return findAll().stream()
                .filter(absences -> absences.getEmployeeID() == employeeID)
                .toList();
    }
}

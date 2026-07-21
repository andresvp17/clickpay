package repository;

import java.util.List;

import entity.Absences;

interface IAbsencesRepo {
    Absences findByID(int id);

    List<Absences> findByEmployeeID(int employeeID);

    void save(Absences absences);

    void update(Absences absences);

    void delete(int id);
}

public class AbsencesRepo implements IAbsencesRepo {
    List<Absences> absences;

    public AbsencesRepo(List<Absences> absences) {
        this.absences = absences;
    }

    public Absences findByID(int id) {
        for (Absences absences : absences) {
            if (absences.getId() == id) {
                return absences;
            }
        }

        return null;
    }

    public List<Absences> findByEmployeeID(int employeeID) {
        return absences.stream().filter(absences -> absences.getEmployeeID() == employeeID).toList();
    }

    public void save(Absences absences) {
        this.absences.add(absences);
    }

    public void update(Absences absence) {
        for (int i = 0; i < absences.size(); i++) {
            if (absences.get(i).getId() == absence.getId()) {
                absences.set(i, absence);
                break;
            }
        }
    }

    public void delete(int id) {
        absences.removeIf(absences -> absences.getId() == id);
    }
}

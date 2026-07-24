package services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import entity.Absences;
import lib.Utility;
import repository.AbsencesRepo;

public class AbsencesService {
    AbsencesRepo absencesRepo;
    EmployeeService employeeService;

    public AbsencesService(AbsencesRepo absencesRepo, EmployeeService employeeService) {
        this.absencesRepo = absencesRepo;
        this.employeeService = employeeService;
    }

    public Optional<Absences> getByID(int id) {
        return absencesRepo.findByID(id);
    }

    public List<Absences> getByEmployeeID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("La ID " + id + " del empleado está mal utilizada");
        }

        return absencesRepo.findByEmployeeID(id);
    }

    public void createAbsence(int employeeID, LocalDate date, String reason, boolean isUnpaid) {
        if (employeeID <= 0) {
            throw new IllegalArgumentException("La ID " + employeeID + " del empleado está mal utilizada");
        }

        if (employeeService.getEmployeeByID(employeeID).isEmpty()) {
            throw new NoSuchElementException("El usuario con la ID " + employeeID + " no existe");
        }

        if (reason.isEmpty()) {
            throw new IllegalArgumentException("La razón de ausencia no puede estar vacía");
        }

        int maxID = Utility.getMaxID(absencesRepo.findAll());

        Absences absences = new Absences(maxID + 1, employeeID, date, reason, isUnpaid);
        absencesRepo.save(absences);
    }

    public void deleteAbsence(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("La ID de la ausencia está mal usada");
        }

        absencesRepo.findByID(id)
                .orElseThrow(() -> new NoSuchElementException("La ausencia con la ID " + id + " no existe"));
        absencesRepo.delete(id);
    }
}

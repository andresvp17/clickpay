import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

import entity.Absences;
import entity.Department;
import entity.Employee;
import entity.Payslip;
import repository.AbsencesRepo;
import repository.DepartmentRepo;
import repository.EmployeeRepo;
import repository.PayslipRepo;
import services.AbsencesService;
import services.DepartmentService;
import services.EmployeeService;
import services.PayslipService;

enum Actions {
    IDLE,
    GET_EMPLOYEE,
    GET_PAYSLIP,
    GET_DEPARTMENT,
    GET_ABSENCE,
    EXIT
}

public class Cli {
    Actions option = Actions.IDLE;
    boolean run = true;

    Scanner sc = new Scanner(System.in);
    Path dataDir = Paths.get(System.getProperty("hr.data.dir", "src/main/resources/db/"));

    EmployeeRepo employeeRepo = new EmployeeRepo(dataDir.resolve("employees.csv"));
    DepartmentRepo departmentRepo = new DepartmentRepo(dataDir.resolve("department.csv"));
    AbsencesRepo absencesRepo = new AbsencesRepo(dataDir.resolve("absences.csv"));
    PayslipRepo payslipRepo = new PayslipRepo(dataDir.resolve("payslips.csv"));

    EmployeeService employeeService = new EmployeeService(employeeRepo);
    DepartmentService departmentService = new DepartmentService(departmentRepo);
    AbsencesService absencesService = new AbsencesService(absencesRepo, employeeService);
    PayslipService payslipService = new PayslipService(payslipRepo);

    public Cli() {
    }

    public void show() {
        System.out.println("========================================");
        System.out.println("======== Bienvenido a ClickPay! ========");
        System.out.println("========================================");
        System.out.println("Seleecione Una de las Siguientes Opciones: ");

        System.out.println("1.- Consultar Información de Empleado");
        System.out.println("2.- Consultar Información de Recibo");
        System.out.println("3.- Consultar Información de Departamento");
        System.out.println("4.- Consultar Información de Ausencia");
    }

    public void getEmployee() {
        System.out.print("Ingrese la ID del usuario: ");
        int id = sc.nextInt();

        Optional<Employee> employee = employeeRepo.findByID(id);

        System.out.printf("ID: %d\t Name: %s\t Lastname: %s\n", employee.get().getID(), employee.get().getName(),
                employee.get().getLastname());
    }

    public void getPayslip() {
        System.out.print("Ingrese la ID del recibo: ");
        int id = sc.nextInt();

        Optional<Payslip> payslip = payslipService.getPayslipByID(id);

        System.out.printf("ID: %d\t EmployeeID: %d\t Period: %s\t GrossPay: %.2f\t Deductions: %.2f\t NetPay: %.2f\n",
                payslip.get().getID(), payslip.get().getEmployeeID(), payslip.get().getPeriod(),
                payslip.get().getGrossPay(), payslip.get().getDeductions(), payslip.get().getNetPay());
    }

    public void getDepartment() {
        System.out.print("Ingrese la ID del departamento: ");
        int id = sc.nextInt();

        Optional<Department> department = departmentRepo.findByID(id);

        System.out.printf("ID: %d\t Name: %s\n", department.get().getID(), department.get().getName());
    }

    public void getAbsence() {
        System.out.print("Ingrese la ID de la ausencia: ");
        int id = sc.nextInt();

        Optional<Absences> absence = absencesRepo.findByID(id);

        System.out.printf("ID: %d\t EmployeeID: %d\t Date: %s\t Reason: %s\n", absence.get().getID(),
                absence.get().getEmployeeID(), absence.get().getDate(), absence.get().getReason());
    }

    public void run() {
        while (run) {
            show();
            option = Actions.values()[sc.nextInt()];
            switch (option) {
                case GET_EMPLOYEE:
                    getEmployee();
                    break;
                case GET_PAYSLIP:
                    getPayslip();
                    break;
                case GET_DEPARTMENT:
                    getDepartment();
                    break;
                case GET_ABSENCE:
                    getAbsence();
                    break;
                case EXIT:
                    run = false;
                    break;
                default:
                    break;
            }
        }
    }
}

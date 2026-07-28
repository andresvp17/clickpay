package ui;

import repository.DepartmentRepo;
import repository.EmployeeRepo;
import repository.PayslipRepo;
import services.DepartmentService;
import services.EmployeeService;
import services.PayslipService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final JLabel titleLabel = new JLabel("Panel de Recibos de Pago");
    private final JLabel subtitleLabel = new JLabel("Gestión centralizada de empleados, departamentos y recibos.");
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();

    public MainWindow() {
        super("Sistema de Recibos de Pago");

        Path dataDir = Paths.get(System.getProperty("hr.data.dir", "src/main/resources/db/"));
        EmployeeRepo employeeRepo = new EmployeeRepo(dataDir.resolve("employees.csv"));
        DepartmentRepo departmentRepo = new DepartmentRepo(dataDir.resolve("department.csv"));
        PayslipRepo payslipRepo = new PayslipRepo(dataDir.resolve("payslips.csv"));

        EmployeeService employeeService = new EmployeeService(employeeRepo);
        DepartmentService departmentService = new DepartmentService(departmentRepo);
        PayslipService payslipService = new PayslipService(payslipRepo, employeeRepo);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(244, 246, 249));

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.setBackground(new Color(244, 246, 249));
        sidebar.setPreferredSize(new Dimension(250, 0));

        JLabel logoLabel = new JLabel("SP Sistema de Pagos");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoLabel.setForeground(new Color(44, 62, 80));

        JLabel menuLabel = new JLabel("Menú Principal");
        menuLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        menuLabel.setForeground(Color.GRAY);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnEmpleados = createMenuButton("👤 Empleados", "employees");
        JButton btnDepartamentos = createMenuButton("🏢 Departamentos", "departments");
        JButton btnRecibos = createMenuButton("📄 Recibos", "payslips");

        menuButtons.put("employees", btnEmpleados);
        menuButtons.put("departments", btnDepartamentos);
        menuButtons.put("payslips", btnRecibos);

        sidebar.add(logoLabel);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnEmpleados);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnDepartamentos);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnRecibos);
        sidebar.add(Box.createVerticalGlue());

        JPanel profileBox = new JPanel();
        profileBox.setLayout(new BoxLayout(profileBox, BoxLayout.Y_AXIS));
        profileBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        profileBox.setBackground(Color.WHITE);

        JLabel userIcon = new JLabel("👤", SwingConstants.CENTER);
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userName = new JLabel("Administrador", SwingConstants.CENTER);
        userName.setFont(new Font("SansSerif", Font.BOLD, 12));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnCerrarSesion.getPreferredSize().height));
        btnCerrarSesion.addActionListener(e -> System.exit(0));

        profileBox.add(userIcon);
        profileBox.add(Box.createVerticalStrut(8));
        profileBox.add(userName);
        profileBox.add(Box.createVerticalStrut(8));
        profileBox.add(btnCerrarSesion);

        sidebar.add(profileBox);
        root.add(sidebar, BorderLayout.WEST);

        JPanel contentArea = new JPanel(new BorderLayout(0, 15));
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        contentArea.setBackground(Color.WHITE);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        subtitleLabel.setForeground(Color.GRAY);
        header.add(titleLabel);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleLabel);

        contentPanel.setOpaque(false);
        contentPanel.add(new EmployeePanel(employeeService, departmentService), "employees");
        contentPanel.add(new DepartmentPanel(employeeService, departmentService), "departments");
        contentPanel.add(new PayslipPanel(payslipService, employeeService, departmentService), "payslips");

        contentArea.add(header, BorderLayout.NORTH);
        contentArea.add(contentPanel, BorderLayout.CENTER);

        JPanel appFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        appFooter.setBackground(new Color(248, 249, 250));
        appFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));
        appFooter.add(new JLabel("🟢 Sistema Conectado"));
        root.add(appFooter, BorderLayout.SOUTH);
        root.add(contentArea, BorderLayout.CENTER);

        setContentPane(root);
        showView("employees");
    }

    private void showView(String viewKey) {
        cardLayout.show(contentPanel, viewKey);
        switch (viewKey) {
            case "employees" -> {
                titleLabel.setText("Empleados");
                subtitleLabel.setText("Consulta integral de los empleados y su departamento asociado.");
            }
            case "departments" -> {
                titleLabel.setText("Departamentos");
                subtitleLabel.setText("Visualiza cada departamento con sus empleados asignados.");
            }
            case "payslips" -> {
                titleLabel.setText("Recibos de Pago");
                subtitleLabel.setText("Selecciona uno o varios recibos para generar sus PDFs.");
            }
            default -> {
                titleLabel.setText("Panel de Recibos de Pago");
                subtitleLabel.setText("Gestión centralizada de empleados, departamentos y recibos.");
            }
        }
        menuButtons.forEach(
                (key, button) -> button.setBackground(key.equals(viewKey) ? new Color(208, 225, 253) : Color.WHITE));
    }

    private JButton createMenuButton(String text, String viewKey) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(51, 51, 51));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        button.addActionListener(e -> showView(viewKey));
        return button;
    }
}

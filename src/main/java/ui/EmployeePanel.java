package ui;

import entity.Department;
import entity.Employee;
import services.DepartmentService;
import services.EmployeeService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class EmployeePanel extends JPanel {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final List<Employee> employees = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public EmployeePanel(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        searchField = new JTextField(20);
        JButton btnAdd = new JButton("Crear");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Eliminar");
        toolbar.add(new JLabel("Buscar empleado:"));
        toolbar.add(searchField);
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);

        String[] columns = { "ID", "Nombre", "Apellido", "Edad", "Salario", "Departamento" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        reloadEmployees(tableModel, employeeService, departmentService);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(tableModel, searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(tableModel, searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter(tableModel, searchField.getText());
            }
        });

        btnAdd.addActionListener(e -> showCreateDialog(tableModel));
        btnEdit.addActionListener(e -> showEditDialog(tableModel, table));
        btnDelete.addActionListener(e -> deleteSelectedEmployee(tableModel, table));

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void reloadEmployees(DefaultTableModel tableModel, EmployeeService employeeService,
            DepartmentService departmentService) {
        employees.clear();
        employees.addAll(employeeService.findAllEmployees());
        tableModel.setRowCount(0);
        for (Employee employee : employees) {
            tableModel.addRow(buildRow(employee));
        }
        if (searchField != null) {
            filter(tableModel, searchField.getText());
        }
    }

    private void filter(DefaultTableModel tableModel, String text) {
        tableModel.setRowCount(0);
        if (text == null || text.trim().isEmpty()) {
            for (Employee employee : employees) {
                tableModel.addRow(buildRow(employee));
            }
            return;
        }

        String query = text.trim().toLowerCase(Locale.ROOT);
        for (Employee employee : employees) {
            String fullName = (employee.getName() + " " + employee.getLastname()).toLowerCase(Locale.ROOT);
            if (fullName.contains(query) || String.valueOf(employee.getID()).contains(query)) {
                tableModel.addRow(buildRow(employee));
            }
        }
    }

    private Object[] buildRow(Employee employee) {
        Department department = departmentService.getDepartmentByID(employee.getDepartmentID()).orElse(null);
        return new Object[] { employee.getID(), employee.getName(), employee.getLastname(), employee.getAge(),
                String.format("€%.2f", employee.getSalary()),
                department != null ? department.getName() : "Sin departamento" };
    }

    private void showCreateDialog(DefaultTableModel tableModel) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField nameField = new JTextField();
        JTextField lastnameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField departmentField = new JTextField();

        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Apellido:"));
        form.add(lastnameField);
        form.add(new JLabel("Edad:"));
        form.add(ageField);
        form.add(new JLabel("Salario:"));
        form.add(salaryField);
        form.add(new JLabel("Departamento ID:"));
        form.add(departmentField);

        int option = JOptionPane.showConfirmDialog(this, form, "Nuevo empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            employeeService.createEmployee(
                    nameField.getText().trim(),
                    lastnameField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    Double.parseDouble(salaryField.getText().trim()),
                    Integer.parseInt(departmentField.getText().trim()));
            reloadEmployees(tableModel, employeeService, departmentService);
            JOptionPane.showMessageDialog(this, "Empleado creado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditDialog(DefaultTableModel tableModel, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para editar.");
            return;
        }

        int selectedId = Integer
                .parseInt(tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0).toString());
        Optional<Employee> optionalEmployee = employeeService.getEmployeeByID(selectedId);
        if (optionalEmployee.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el empleado.");
            return;
        }

        Employee employee = optionalEmployee.get();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField nameField = new JTextField(employee.getName());
        JTextField lastnameField = new JTextField(employee.getLastname());
        JTextField ageField = new JTextField(String.valueOf(employee.getAge()));
        JTextField salaryField = new JTextField(String.valueOf(employee.getSalary()));
        JTextField departmentField = new JTextField(String.valueOf(employee.getDepartmentID()));

        form.add(new JLabel("Nombre:"));
        form.add(nameField);
        form.add(new JLabel("Apellido:"));
        form.add(lastnameField);
        form.add(new JLabel("Edad:"));
        form.add(ageField);
        form.add(new JLabel("Salario:"));
        form.add(salaryField);
        form.add(new JLabel("Departamento ID:"));
        form.add(departmentField);

        int option = JOptionPane.showConfirmDialog(this, form, "Editar empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            employeeService.updateEmployee(
                    employee.getID(),
                    nameField.getText().trim(),
                    lastnameField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    Double.parseDouble(salaryField.getText().trim()),
                    Integer.parseInt(departmentField.getText().trim()));
            reloadEmployees(tableModel, employeeService, departmentService);
            JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedEmployee(DefaultTableModel tableModel, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar.");
            return;
        }

        int selectedId = Integer
                .parseInt(tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el empleado con ID " + selectedId + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            employeeService.deleteEmployee(selectedId);
            reloadEmployees(tableModel, employeeService, departmentService);
            JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

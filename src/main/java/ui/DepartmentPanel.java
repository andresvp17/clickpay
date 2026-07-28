package ui;

import entity.Department;
import entity.Employee;
import services.DepartmentService;
import services.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class DepartmentPanel extends JPanel {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private DefaultTableModel tableModel;
    private JTable table;

    public DepartmentPanel(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        JButton btnAdd = new JButton("Crear");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Eliminar");
        toolbar.add(btnAdd);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);

        String[] columns = { "ID", "Departamento", "Empleado", "Salario" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        reloadDepartments(tableModel);

        btnAdd.addActionListener(e -> showCreateDialog(tableModel));
        btnEdit.addActionListener(e -> showEditDialog(tableModel, table));
        btnDelete.addActionListener(e -> deleteSelectedDepartment(tableModel, table));

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void reloadDepartments(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Department> departments = departmentService.getAllDepartments();
        for (Department department : departments) {
            List<Employee> employees = employeeService.findAllEmployees().stream()
                    .filter(employee -> employee.getDepartmentID() == department.getID())
                    .toList();

            if (employees.isEmpty()) {
                tableModel.addRow(new Object[] { department.getID(), department.getName(), "Sin empleados", "-" });
                continue;
            }

            for (Employee employee : employees) {
                tableModel.addRow(new Object[] { department.getID(), department.getName(),
                        employee.getName() + " " + employee.getLastname(),
                        String.format("€%.2f", employee.getSalary()) });
            }
        }
    }

    private void showCreateDialog(DefaultTableModel tableModel) {
        JTextField nameField = new JTextField();
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);

        int option = JOptionPane.showConfirmDialog(this, form, "Nuevo departamento", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            departmentService.createDepartment(nameField.getText().trim());
            reloadDepartments(tableModel);
            JOptionPane.showMessageDialog(this, "Departamento creado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditDialog(DefaultTableModel tableModel, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para editar.");
            return;
        }

        int selectedId = Integer
                .parseInt(tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0).toString());
        Optional<Department> optionalDepartment = departmentService.getDepartmentByID(selectedId);
        if (optionalDepartment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el departamento.");
            return;
        }

        Department department = optionalDepartment.get();
        JTextField nameField = new JTextField(department.getName());
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Nombre:"));
        form.add(nameField);

        int option = JOptionPane.showConfirmDialog(this, form, "Editar departamento", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            departmentService.updateDepartment(department.getID(), nameField.getText().trim());
            reloadDepartments(tableModel);
            JOptionPane.showMessageDialog(this, "Departamento actualizado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedDepartment(DefaultTableModel tableModel, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para eliminar.");
            return;
        }

        int selectedId = Integer
                .parseInt(tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar el departamento con ID " + selectedId + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            departmentService.deleteDepartment(selectedId);
            reloadDepartments(tableModel);
            JOptionPane.showMessageDialog(this, "Departamento eliminado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

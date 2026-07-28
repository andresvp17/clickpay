package ui;

import entity.Department;
import entity.Employee;
import entity.Payslip;
import services.DepartmentService;
import services.EmployeeService;
import services.PayslipService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.IntStream;

public class PayslipPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final PayslipService payslipService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public PayslipPanel(PayslipService payslipService, EmployeeService employeeService,
            DepartmentService departmentService) {
        this.payslipService = payslipService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;

        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        JCheckBox selectAll = new JCheckBox("Seleccionar todo");
        JButton btnDownload = new JButton("Descargar seleccionados");
        btnDownload.addActionListener(e -> downloadSelectedPayslips());
        toolbar.add(selectAll);
        toolbar.add(btnDownload);

        String[] columns = { "ID", "Empleado", "Departamento", "Periodo", "Neto", "Seleccionar" };
        this.tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // only the checkbox column is editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return Integer.class; // ID
                if (columnIndex == 4)
                    return String.class; // Neto (formatted)
                if (columnIndex == 5)
                    return Boolean.class; // checkbox
                return String.class;
            }
        };

        JTable table = new JTable(this.tableModel);
        // Ensure checkbox column shows checkboxes and is editable
        TableColumnModel colModel = table.getColumnModel();
        TableColumn checkboxCol = colModel.getColumn(5);
        checkboxCol.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        checkboxCol.setCellRenderer(new TableCellRenderer() {
            private final JCheckBox checkbox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                checkbox.setSelected(Boolean.TRUE.equals(value));
                checkbox.setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected)
                    checkbox.setBackground(table.getSelectionBackground());
                else
                    checkbox.setBackground(table.getBackground());
                return checkbox;
            }
        });

        // double-click to generate single payslip
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int viewRow = table.rowAtPoint(e.getPoint());
                    if (viewRow >= 0) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        Object idVal = tableModel.getValueAt(modelRow, 0);
                        if (idVal != null) {
                            int payslipId = Integer.parseInt(idVal.toString());
                            int confirm = JOptionPane.showConfirmDialog(PayslipPanel.this,
                                    "Generar PDF para el recibo ID " + payslipId + "?",
                                    "Confirmar generación", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                try {
                                    payslipService.generate(payslipId);
                                    JOptionPane.showMessageDialog(PayslipPanel.this,
                                            "Recibo " + payslipId + " generado correctamente.");
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(PayslipPanel.this,
                                            "Error generando recibo: " + ex.getMessage(), "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // select-all checkbox behavior
        selectAll.addActionListener(ae -> {
            boolean sel = selectAll.isSelected();
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                tableModel.setValueAt(sel, r, 5);
            }
            // fire change so any listeners react
            tableModel.fireTableChanged(new TableModelEvent(tableModel));
        });

        reloadPayslips(tableModel);

        add(toolbar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void reloadPayslips(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Payslip> payslips = payslipService.getAllPayslip();
        for (Payslip payslip : payslips) {
            Employee employee = employeeService.getEmployeeByID(payslip.getEmployeeID()).orElse(null);
            Department department = employee != null
                    ? departmentService.getDepartmentByID(employee.getDepartmentID()).orElse(null)
                    : null;
            String employeeName = employee != null ? employee.getName() + " " + employee.getLastname()
                    : "Empleado no encontrado";
            String departmentName = department != null ? department.getName() : "Sin departamento";
            tableModel.addRow(new Object[] { payslip.getID(), employeeName, departmentName,
                    payslip.getPeriod(), String.format("€%.2f", payslip.getNetPay()), false });
        }
    }

    private void downloadSelectedPayslips() {
        int rowCount = tableModel.getRowCount();
        int generated = 0;

        for (int i = 0; i < rowCount; i++) {
            boolean selected = Boolean.TRUE.equals(tableModel.getValueAt(i, 5));
            if (!selected) {
                continue;
            }

            int payslipId = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
            try {
                payslipService.generate(payslipId);
                generated++;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo generar el recibo " + payslipId + ": " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (generated == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un recibo para generar.");
        } else {
            JOptionPane.showMessageDialog(this, "Se generaron " + generated + " recibos correctamente.");
        }
    }
}

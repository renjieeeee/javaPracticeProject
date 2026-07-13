package com.hospital.gui;

import com.hospital.entity.*;
import com.hospital.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {
    private DepartmentService departmentService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public AdminFrame() {
        this.departmentService = new DepartmentService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
        initUI();
    }

    private void initUI() {
        setTitle("管理员功能 - 医院门诊预约挂号系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("管理员控制台", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(8, 1, 8, 10));
        menuPanel.setPreferredSize(new Dimension(160, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        menuPanel.setBackground(new Color(230, 240, 250));

        JButton btnViewDepts = createMenuButton("查看科室");
        JButton btnAddDept = createMenuButton("添加科室");
        JButton btnEditDept = createMenuButton("修改科室");
        JButton btnDelDept = createMenuButton("删除科室");
        JButton btnViewDoctors = createMenuButton("查看医生");
        JButton btnAddDoctor = createMenuButton("添加医生");
        JButton btnViewAppoints = createMenuButton("预约管理");
        JButton btnExport = createMenuButton("导出记录");
        JButton btnLogout = createMenuButton("退出登录");

        btnViewDepts.addActionListener(e -> viewDepartments());
        btnAddDept.addActionListener(e -> addDepartment());
        btnEditDept.addActionListener(e -> updateDepartment());
        btnDelDept.addActionListener(e -> deleteDepartment());
        btnViewDoctors.addActionListener(e -> viewDoctors());
        btnAddDoctor.addActionListener(e -> addDoctor());
        btnViewAppoints.addActionListener(e -> appointmentManagement());
        btnExport.addActionListener(e -> exportAppointments());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        menuPanel.add(btnViewDepts);
        menuPanel.add(btnAddDept);
        menuPanel.add(btnEditDept);
        menuPanel.add(btnDelDept);
        menuPanel.add(btnViewDoctors);
        menuPanel.add(btnAddDoctor);
        menuPanel.add(btnViewAppoints);
        menuPanel.add(btnExport);
        menuPanel.add(btnLogout);

        mainPanel.add(menuPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        dataTable.setRowHeight(28);
        dataTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("数据展示"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("  提示：请选择左侧功能菜单进行操作");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
        viewDepartments();
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void viewDepartments() {
        String[] columns = {"ID", "科室编号", "科室名称"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        List<Department> departments = departmentService.findAll();
        for (Department dept : departments) {
            tableModel.addRow(new Object[]{dept.getId(), dept.getDeptCode(), dept.getDeptName()});
        }
    }

    private void addDepartment() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        panel.add(new JLabel("科室编号:"));
        panel.add(codeField);
        panel.add(new JLabel("科室名称:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "添加科室", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "科室编号和名称不能为空！");
                return;
            }
            if (departmentService.addDepartment(code, name)) {
                JOptionPane.showMessageDialog(this, "科室添加成功！");
                viewDepartments();
            } else {
                JOptionPane.showMessageDialog(this, "科室添加失败，编号可能已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDepartment() {
        viewDepartments();
        String code = JOptionPane.showInputDialog(this, "请输入要修改的科室编号:");
        if (code == null || code.trim().isEmpty()) return;

        Department dept = departmentService.findByCode(code.trim());
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "科室编号不存在！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "请输入新的科室名称:", dept.getDeptName());
        if (newName == null || newName.trim().isEmpty()) return;

        if (departmentService.updateDepartment(code.trim(), newName.trim())) {
            JOptionPane.showMessageDialog(this, "科室修改成功！");
            viewDepartments();
        } else {
            JOptionPane.showMessageDialog(this, "科室修改失败！");
        }
    }

    private void deleteDepartment() {
        viewDepartments();
        String code = JOptionPane.showInputDialog(this, "请输入要删除的科室编号:");
        if (code == null || code.trim().isEmpty()) return;

        int result = JOptionPane.showConfirmDialog(this, "确认删除该科室？", "确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (departmentService.deleteDepartment(code.trim())) {
                JOptionPane.showMessageDialog(this, "科室删除成功！");
                viewDepartments();
            } else {
                JOptionPane.showMessageDialog(this, "科室删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewDoctors() {
        String[] columns = {"ID", "工号", "姓名", "科室", "职称", "状态"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        List<Doctor> doctors = doctorService.findAll();
        for (Doctor doc : doctors) {
            tableModel.addRow(new Object[]{
                    doc.getId(), doc.getDoctorCode(), doc.getDoctorName(),
                    doc.getDeptName(), doc.getTitle(), doc.isStatus() ? "正常" : "停诊"
            });
        }
    }

    private void addDoctor() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先添加科室！");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();

        String[] deptOptions = new String[departments.size()];
        for (int i = 0; i < departments.size(); i++) {
            deptOptions[i] = departments.get(i).getDeptCode() + " - " + departments.get(i).getDeptName();
        }
        JComboBox<String> deptCombo = new JComboBox<>(deptOptions);

        String[] titles = {"主任医师", "副主任医师", "主治医师", "住院医师"};
        JComboBox<String> titleCombo = new JComboBox<>(titles);

        panel.add(new JLabel("医生工号:"));
        panel.add(codeField);
        panel.add(new JLabel("医生姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("所属科室:"));
        panel.add(deptCombo);
        panel.add(new JLabel("职称:"));
        panel.add(titleCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "添加医生", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String deptStr = (String) deptCombo.getSelectedItem();
            String title = (String) titleCombo.getSelectedItem();

            if (code.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "工号和姓名不能为空！");
                return;
            }

            String deptCode = deptStr.split(" - ")[0];
            Department dept = departmentService.findByCode(deptCode);

            if (doctorService.addDoctor(code, name, dept.getId(), title)) {
                JOptionPane.showMessageDialog(this, "医生添加成功！");
                viewDoctors();
            } else {
                JOptionPane.showMessageDialog(this, "医生添加失败，工号可能已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void appointmentManagement() {
        String[] columns = {"预约编号", "用户姓名", "医生姓名", "科室", "预约日期", "时间段", "状态"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        List<Appointment> appointments = appointmentService.findAll();
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getUserName(), a.getDoctorName(), a.getDeptName(),
                    a.getAppointTime().toLocalDate(), a.getTimeSlot(), a.getStatus()
            });
        }

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("输入预约编号更新状态为\"已就诊\":"));
        JTextField idField = new JTextField(8);
        JButton updateBtn = new JButton("更新状态");
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                int confirm = JOptionPane.showConfirmDialog(this, "确认将该预约状态更新为\"已就诊\"？", "确认", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (appointmentService.completeAppointment(id)) {
                        JOptionPane.showMessageDialog(this, "状态更新成功！");
                        appointmentManagement();
                    } else {
                        JOptionPane.showMessageDialog(this, "更新失败！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(idField);
        panel.add(updateBtn);

        JOptionPane.showMessageDialog(this, panel, "预约管理", JOptionPane.PLAIN_MESSAGE);
    }

    private void exportAppointments() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择导出路径");
        fileChooser.setSelectedFile(new java.io.File("appointments.txt"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (appointmentService.exportToFile(filePath)) {
                JOptionPane.showMessageDialog(this, "预约记录已成功导出到:\n" + filePath);
            } else {
                JOptionPane.showMessageDialog(this, "导出失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

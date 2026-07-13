package com.hospital.gui;

import com.hospital.entity.*;
import com.hospital.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserFrame extends JFrame {
    private User currentUser;
    private DepartmentService departmentService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00",
            "10:00-10:30", "10:30-11:00", "11:00-11:30",
            "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00",
            "16:00-16:30", "16:30-17:00"
    );

    public UserFrame(User user) {
        this.currentUser = user;
        this.departmentService = new DepartmentService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
        initUI();
    }

    private void initUI() {
        setTitle("用户功能 - " + currentUser.getRealName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("欢迎, " + currentUser.getRealName() + " (普通用户)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        menuPanel.setPreferredSize(new Dimension(160, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menuPanel.setBackground(new Color(230, 240, 250));

        JButton btnViewDept = createMenuButton("查看科室医生");
        JButton btnMakeAppoint = createMenuButton("预约挂号");
        JButton btnMyAppoints = createMenuButton("我的预约");
        JButton btnCancelAppoint = createMenuButton("取消预约");
        JButton btnLogout = createMenuButton("退出登录");

        btnViewDept.addActionListener(e -> viewDepartmentsAndDoctors());
        btnMakeAppoint.addActionListener(e -> makeAppointment());
        btnMyAppoints.addActionListener(e -> viewMyAppointments());
        btnCancelAppoint.addActionListener(e -> cancelAppointment());
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        menuPanel.add(btnViewDept);
        menuPanel.add(btnMakeAppoint);
        menuPanel.add(btnMyAppoints);
        menuPanel.add(btnCancelAppoint);
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

        JLabel statusLabel = new JLabel("  提示：请选择左侧功能菜单");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
        viewDepartmentsAndDoctors();
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 13));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void viewDepartmentsAndDoctors() {
        String[] columns = {"科室编号", "科室名称", "医生工号", "医生姓名", "职称", "状态"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        List<Department> departments = departmentService.findAll();
        for (Department dept : departments) {
            List<Doctor> doctors = doctorService.findByDeptId(dept.getId());
            if (doctors.isEmpty()) {
                tableModel.addRow(new Object[]{dept.getDeptCode(), dept.getDeptName(), "-", "-", "-", "-"});
            } else {
                for (Doctor doc : doctors) {
                    tableModel.addRow(new Object[]{
                            dept.getDeptCode(), dept.getDeptName(),
                            doc.getDoctorCode(), doc.getDoctorName(),
                            doc.getTitle(), doc.isStatus() ? "正常" : "停诊"
                    });
                }
            }
        }
    }

    private void makeAppointment() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "暂无科室信息");
            return;
        }

        String[] deptNames = new String[departments.size()];
        for (int i = 0; i < departments.size(); i++) {
            deptNames[i] = departments.get(i).getDeptCode() + " - " + departments.get(i).getDeptName();
        }

        String selectedDept = (String) JOptionPane.showInputDialog(this, "请选择科室:", "选择科室",
                JOptionPane.PLAIN_MESSAGE, null, deptNames, deptNames[0]);
        if (selectedDept == null) return;

        String deptCode = selectedDept.split(" - ")[0];
        Department dept = departmentService.findByCode(deptCode);

        List<Doctor> doctors = doctorService.findByDeptId(dept.getId());
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(this, "该科室暂无可预约的医生");
            return;
        }

        String[] doctorItems = new String[doctors.size()];
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doc = doctors.get(i);
            doctorItems[i] = doc.getDoctorCode() + " - " + doc.getDoctorName() + " (" + doc.getTitle() + ")";
        }

        String selectedDoctor = (String) JOptionPane.showInputDialog(this, "请选择医生:", "选择医生",
                JOptionPane.PLAIN_MESSAGE, null, doctorItems, doctorItems[0]);
        if (selectedDoctor == null) return;

        String doctorCode = selectedDoctor.split(" - ")[0];
        Doctor doctor = doctorService.findByCode(doctorCode);

        String dateStr = JOptionPane.showInputDialog(this, "请输入预约日期 (格式: yyyy-MM-dd):", "选择日期", JOptionPane.PLAIN_MESSAGE);
        if (dateStr == null || dateStr.trim().isEmpty()) return;

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "日期格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "不能预约过去的日期！");
            return;
        }

        String[] slotArray = TIME_SLOTS.toArray(new String[0]);
        String selectedSlot = (String) JOptionPane.showInputDialog(this, "请选择时间段:", "选择时间",
                JOptionPane.PLAIN_MESSAGE, null, slotArray, slotArray[0]);
        if (selectedSlot == null) return;

        int hour = Integer.parseInt(selectedSlot.split(":")[0]);
        LocalDateTime appointTime = LocalDateTime.of(date, LocalTime.of(hour, 0));

        String confirmMsg = String.format("确认预约？\n\n科室: %s\n医生: %s (%s)\n日期: %s\n时间: %s",
                dept.getDeptName(), doctor.getDoctorName(), doctor.getTitle(), date, selectedSlot);

        int result = JOptionPane.showConfirmDialog(this, confirmMsg, "确认预约", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            if (appointmentService.createAppointment(currentUser.getId(), doctor.getId(), appointTime, selectedSlot)) {
                JOptionPane.showMessageDialog(this, "预约成功！");
                viewMyAppointments();
            } else {
                JOptionPane.showMessageDialog(this, "预约失败！");
            }
        }
    }

    private void viewMyAppointments() {
        String[] columns = {"预约编号", "医生姓名", "科室", "预约日期", "时间段", "状态"};
        tableModel.setColumnIdentifiers(columns);
        tableModel.setRowCount(0);

        List<Appointment> appointments = appointmentService.findByUserId(currentUser.getId());
        for (Appointment a : appointments) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getDoctorName(), a.getDeptName(),
                    a.getAppointTime().toLocalDate(), a.getTimeSlot(), a.getStatus()
            });
        }

        if (appointments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "暂无预约记录");
        }
    }

    private void cancelAppointment() {
        viewMyAppointments();

        String input = JOptionPane.showInputDialog(this, "请输入要取消的预约编号:");
        if (input == null || input.trim().isEmpty()) return;

        try {
            int appointmentId = Integer.parseInt(input.trim());
            int result = JOptionPane.showConfirmDialog(this, "确认取消该预约？", "确认", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (appointmentService.cancelAppointment(appointmentId, currentUser.getId())) {
                    JOptionPane.showMessageDialog(this, "预约已取消！");
                    viewMyAppointments();
                } else {
                    JOptionPane.showMessageDialog(this, "取消失败，预约不存在或状态不允许取消！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}

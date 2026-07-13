package com.hospital.ui;

import com.hospital.entity.*;
import com.hospital.service.AppointmentService;
import com.hospital.service.DepartmentService;
import com.hospital.service.DoctorService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserUI {
    private final Scanner scanner;
    private final User currentUser;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    private static final List<String> TIME_SLOTS = Arrays.asList(
            "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00",
            "10:00-10:30", "10:30-11:00", "11:00-11:30",
            "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00",
            "16:00-16:30", "16:30-17:00"
    );

    public UserUI(Scanner scanner, User user) {
        this.scanner = scanner;
        this.currentUser = user;
        this.departmentService = new DepartmentService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
    }

    public void show() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         用户功能菜单                 ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. 查看科室及医生列表               ║");
            System.out.println("║  2. 预约挂号                         ║");
            System.out.println("║  3. 查看我的预约记录                 ║");
            System.out.println("║  4. 取消预约                         ║");
            System.out.println("║  0. 退出登录                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewDepartmentsAndDoctors();
                    break;
                case "2":
                    makeAppointment();
                    break;
                case "3":
                    viewMyAppointments();
                    break;
                case "4":
                    cancelAppointment();
                    break;
                case "0":
                    System.out.println("已退出登录");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    private void viewDepartmentsAndDoctors() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            System.out.println("暂无科室信息");
            return;
        }

        System.out.println("\n========== 科室列表 ==========");
        System.out.printf("%-8s %-15s%n", "科室编号", "科室名称");
        System.out.println("------------------------------");
        for (Department dept : departments) {
            System.out.printf("%-8s %-15s%n", dept.getDeptCode(), dept.getDeptName());
        }

        System.out.print("\n请输入科室编号查看医生（或直接回车返回）: ");
        String code = scanner.nextLine().trim();
        if (code.isEmpty()) return;

        Department dept = departmentService.findByCode(code);
        if (dept == null) {
            System.out.println("科室编号不存在！");
            return;
        }

        List<Doctor> doctors = doctorService.findByDeptId(dept.getId());
        if (doctors.isEmpty()) {
            System.out.println("该科室暂无医生");
            return;
        }

        System.out.println("\n========== " + dept.getDeptName() + " - 医生列表 ==========");
        System.out.printf("%-8s %-10s %-15s %-10s %-6s%n", "工号", "姓名", "科室", "职称", "状态");
        System.out.println("--------------------------------------------------------------");
        for (Doctor doc : doctors) {
            System.out.printf("%-8s %-10s %-15s %-10s %-6s%n",
                    doc.getDoctorCode(), doc.getDoctorName(), doc.getDeptName(), doc.getTitle(), doc.isStatus() ? "正常" : "停诊");
        }
    }

    private void makeAppointment() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            System.out.println("暂无科室信息，无法预约");
            return;
        }

        System.out.println("\n========== 选择科室 ==========");
        for (Department dept : departments) {
            System.out.println(dept.getDeptCode() + " - " + dept.getDeptName());
        }
        System.out.print("请输入科室编号: ");
        String deptCode = scanner.nextLine().trim();
        Department dept = departmentService.findByCode(deptCode);
        if (dept == null) {
            System.out.println("科室编号不存在！");
            return;
        }

        List<Doctor> doctors = doctorService.findByDeptId(dept.getId());
        if (doctors.isEmpty()) {
            System.out.println("该科室暂无可预约的医生");
            return;
        }

        System.out.println("\n========== 选择医生 ==========");
        for (Doctor doc : doctors) {
            System.out.printf("%-8s %-10s %-10s%n", doc.getDoctorCode(), doc.getDoctorName(), doc.getTitle());
        }
        System.out.print("请输入医生工号: ");
        String doctorCode = scanner.nextLine().trim();
        Doctor doctor = doctorService.findByCode(doctorCode) != null ? doctorService.findByCode(doctorCode) : null;
        if (doctor == null || doctor.getDeptId() != dept.getId()) {
            System.out.println("医生工号无效！");
            return;
        }

        System.out.print("请输入预约日期 (格式: yyyy-MM-dd): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.out.println("日期格式错误！");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            System.out.println("不能预约过去的日期！");
            return;
        }

        System.out.println("\n========== 选择时间段 ==========");
        for (int i = 0; i < TIME_SLOTS.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, TIME_SLOTS.get(i));
        }
        System.out.print("请选择时间段编号: ");
        int slotIndex;
        try {
            slotIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (slotIndex < 0 || slotIndex >= TIME_SLOTS.size()) {
                System.out.println("无效选择！");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("请输入数字！");
            return;
        }

        String timeSlot = TIME_SLOTS.get(slotIndex);
        int hour = Integer.parseInt(timeSlot.split(":")[0]);
        LocalDateTime appointTime = LocalDateTime.of(date, LocalTime.of(hour, 0));

        System.out.println("\n========== 预约确认 ==========");
        System.out.println("科室: " + dept.getDeptName());
        System.out.println("医生: " + doctor.getDoctorName() + " (" + doctor.getTitle() + ")");
        System.out.println("日期: " + date);
        System.out.println("时间: " + timeSlot);
        System.out.print("确认预约？(y/n): ");
        String confirm = scanner.nextLine().trim();

        if ("y".equalsIgnoreCase(confirm)) {
            if (appointmentService.createAppointment(currentUser.getId(), doctor.getId(), appointTime, timeSlot)) {
                System.out.println("预约成功！");
            } else {
                System.out.println("预约失败！");
            }
        } else {
            System.out.println("已取消预约操作");
        }
    }

    private void viewMyAppointments() {
        List<Appointment> appointments = appointmentService.findByUserId(currentUser.getId());
        if (appointments.isEmpty()) {
            System.out.println("暂无预约记录");
            return;
        }

        System.out.println("\n========== 我的预约记录 ==========");
        System.out.printf("%-6s %-10s %-8s %-15s %-20s %-8s%n", "编号", "用户", "医生", "科室", "预约时间", "状态");
        System.out.println("----------------------------------------------------------------------");
        for (Appointment a : appointments) {
            System.out.printf("%-6d %-10s %-8s %-15s %-20s %-8s%n",
                    a.getId(), a.getUserName(), a.getDoctorName(), a.getDeptName(),
                    a.getAppointTime().toLocalDate() + " " + a.getTimeSlot(), a.getStatus());
        }
    }

    private void cancelAppointment() {
        viewMyAppointments();
        System.out.print("\n请输入要取消的预约编号（或直接回车返回）: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        try {
            int appointmentId = Integer.parseInt(input);
            System.out.print("确认取消该预约？(y/n): ");
            String confirm = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(confirm)) {
                if (appointmentService.cancelAppointment(appointmentId, currentUser.getId())) {
                    System.out.println("预约已取消！");
                } else {
                    System.out.println("取消失败，预约不存在或状态不允许取消！");
                }
            } else {
                System.out.println("已取消操作");
            }
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字！");
        }
    }
}

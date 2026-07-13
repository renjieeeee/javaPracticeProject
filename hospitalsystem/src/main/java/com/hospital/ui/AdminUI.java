package com.hospital.ui;

import com.hospital.entity.*;
import com.hospital.service.AppointmentService;
import com.hospital.service.DepartmentService;
import com.hospital.service.DoctorService;

import java.util.List;
import java.util.Scanner;

public class AdminUI {
    private final Scanner scanner;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public AdminUI(Scanner scanner) {
        this.scanner = scanner;
        this.departmentService = new DepartmentService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
    }

    public void show() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         管理员功能菜单               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. 科室管理                         ║");
            System.out.println("║  2. 医生管理                         ║");
            System.out.println("║  3. 预约记录管理                     ║");
            System.out.println("║  4. 导出预约记录                     ║");
            System.out.println("║  0. 退出登录                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    departmentManagement();
                    break;
                case "2":
                    doctorManagement();
                    break;
                case "3":
                    appointmentManagement();
                    break;
                case "4":
                    exportAppointments();
                    break;
                case "0":
                    System.out.println("已退出登录");
                    return;
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    private void departmentManagement() {
        while (true) {
            System.out.println("\n----- 科室管理 -----");
            System.out.println("1. 查看所有科室");
            System.out.println("2. 添加科室");
            System.out.println("3. 修改科室");
            System.out.println("4. 删除科室");
            System.out.println("0. 返回");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listDepartments();
                    break;
                case "2":
                    addDepartment();
                    break;
                case "3":
                    updateDepartment();
                    break;
                case "4":
                    deleteDepartment();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("无效选择");
            }
        }
    }

    private void listDepartments() {
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
    }

    private void addDepartment() {
        System.out.print("请输入科室编号: ");
        String code = scanner.nextLine().trim();
        System.out.print("请输入科室名称: ");
        String name = scanner.nextLine().trim();

        if (code.isEmpty() || name.isEmpty()) {
            System.out.println("科室编号和名称不能为空！");
            return;
        }

        if (departmentService.addDepartment(code, name)) {
            System.out.println("科室添加成功！");
        } else {
            System.out.println("科室添加失败，编号可能已存在！");
        }
    }

    private void updateDepartment() {
        listDepartments();
        System.out.print("请输入要修改的科室编号: ");
        String code = scanner.nextLine().trim();
        System.out.print("请输入新的科室名称: ");
        String name = scanner.nextLine().trim();

        if (departmentService.updateDepartment(code, name)) {
            System.out.println("科室修改成功！");
        } else {
            System.out.println("科室修改失败，编号不存在！");
        }
    }

    private void deleteDepartment() {
        listDepartments();
        System.out.print("请输入要删除的科室编号: ");
        String code = scanner.nextLine().trim();
        System.out.print("确认删除？(y/n): ");
        String confirm = scanner.nextLine().trim();

        if ("y".equalsIgnoreCase(confirm)) {
            if (departmentService.deleteDepartment(code)) {
                System.out.println("科室删除成功！");
            } else {
                System.out.println("科室删除失败！");
            }
        }
    }

    private void doctorManagement() {
        while (true) {
            System.out.println("\n----- 医生管理 -----");
            System.out.println("1. 查看所有医生");
            System.out.println("2. 添加医生");
            System.out.println("3. 修改医生信息");
            System.out.println("4. 停诊/恢复");
            System.out.println("0. 返回");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listDoctors();
                    break;
                case "2":
                    addDoctor();
                    break;
                case "3":
                    updateDoctor();
                    break;
                case "4":
                    toggleDoctorStatus();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("无效选择");
            }
        }
    }

    private void listDoctors() {
        List<Doctor> doctors = doctorService.findAll();
        if (doctors.isEmpty()) {
            System.out.println("暂无医生信息");
            return;
        }
        System.out.println("\n========== 医生列表 ==========");
        System.out.printf("%-8s %-10s %-15s %-10s %-6s%n", "工号", "姓名", "科室", "职称", "状态");
        System.out.println("--------------------------------------------------------------");
        for (Doctor doc : doctors) {
            System.out.printf("%-8s %-10s %-15s %-10s %-6s%n",
                    doc.getDoctorCode(), doc.getDoctorName(), doc.getDeptName(), doc.getTitle(), doc.isStatus() ? "正常" : "停诊");
        }
    }

    private void addDoctor() {
        listDepartments();
        System.out.print("请输入医生工号: ");
        String code = scanner.nextLine().trim();
        System.out.print("请输入医生姓名: ");
        String name = scanner.nextLine().trim();
        System.out.print("请输入科室编号: ");
        String deptCode = scanner.nextLine().trim();
        System.out.print("请输入职称（主任医师/副主任医师/主治医师/住院医师）: ");
        String title = scanner.nextLine().trim();

        Department dept = departmentService.findByCode(deptCode);
        if (dept == null) {
            System.out.println("科室编号不存在！");
            return;
        }

        if (doctorService.addDoctor(code, name, dept.getId(), title)) {
            System.out.println("医生添加成功！");
        } else {
            System.out.println("医生添加失败，工号可能已存在！");
        }
    }

    private void updateDoctor() {
        listDoctors();
        System.out.print("请输入要修改的医生工号: ");
        String code = scanner.nextLine().trim();
        System.out.print("请输入新的姓名: ");
        String name = scanner.nextLine().trim();
        System.out.print("请输入新的科室编号: ");
        String deptCode = scanner.nextLine().trim();
        System.out.print("请输入新的职称: ");
        String title = scanner.nextLine().trim();

        Department dept = departmentService.findByCode(deptCode);
        if (dept == null) {
            System.out.println("科室编号不存在！");
            return;
        }

        if (doctorService.updateDoctor(code, name, dept.getId(), title)) {
            System.out.println("医生信息修改成功！");
        } else {
            System.out.println("医生信息修改失败！");
        }
    }

    private void toggleDoctorStatus() {
        listDoctors();
        System.out.print("请输入医生ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (doctorService.toggleStatus(id)) {
                System.out.println("状态更新成功！");
            } else {
                System.out.println("状态更新失败！");
            }
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字！");
        }
    }

    private void appointmentManagement() {
        while (true) {
            System.out.println("\n----- 预约记录管理 -----");
            System.out.println("1. 查看所有预约记录");
            System.out.println("2. 更新预约状态");
            System.out.println("0. 返回");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listAllAppointments();
                    break;
                case "2":
                    updateAppointmentStatus();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("无效选择");
            }
        }
    }

    private void listAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        if (appointments.isEmpty()) {
            System.out.println("暂无预约记录");
            return;
        }
        System.out.println("\n========== 所有预约记录 ==========");
        System.out.printf("%-6s %-10s %-8s %-15s %-20s %-8s%n", "编号", "用户", "医生", "科室", "预约时间", "状态");
        System.out.println("----------------------------------------------------------------------");
        for (Appointment a : appointments) {
            System.out.printf("%-6d %-10s %-8s %-15s %-20s %-8s%n",
                    a.getId(), a.getUserName(), a.getDoctorName(), a.getDeptName(),
                    a.getAppointTime().toLocalDate() + " " + a.getTimeSlot(), a.getStatus());
        }
    }

    private void updateAppointmentStatus() {
        listAllAppointments();
        System.out.print("请输入预约编号: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("确认将该预约状态更新为\"已就诊\"？(y/n): ");
            String confirm = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(confirm)) {
                if (appointmentService.completeAppointment(id)) {
                    System.out.println("状态更新成功！");
                } else {
                    System.out.println("状态更新失败，预约不存在或状态不允许更新！");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字！");
        }
    }

    private void exportAppointments() {
        System.out.print("请输入导出文件路径（如: D:\\appointments.txt）: ");
        String filePath = scanner.nextLine().trim();
        if (filePath.isEmpty()) {
            filePath = "appointments.txt";
        }

        if (appointmentService.exportToFile(filePath)) {
            System.out.println("预约记录已成功导出到: " + filePath);
        } else {
            System.out.println("导出失败！");
        }
    }
}

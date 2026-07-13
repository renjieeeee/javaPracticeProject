package com.hospital.service;

import com.hospital.dao.AppointmentDao;
import com.hospital.entity.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentDao appointmentDao = new AppointmentDao();

    public List<Appointment> findByUserId(int userId) {
        return appointmentDao.findByUserId(userId);
    }

    public List<Appointment> findAll() {
        return appointmentDao.findAll();
    }

    public boolean createAppointment(int userId, int doctorId, LocalDateTime appointTime, String timeSlot) {
        Appointment appointment = new Appointment(userId, doctorId, appointTime, timeSlot);
        return appointmentDao.insert(appointment);
    }

    public boolean cancelAppointment(int appointmentId, int userId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null || appointment.getUserId() != userId) {
            return false;
        }
        if (!"待就诊".equals(appointment.getStatus())) {
            return false;
        }
        return appointmentDao.updateStatus(appointmentId, "已取消");
    }

    public boolean completeAppointment(int appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null || !"待就诊".equals(appointment.getStatus())) {
            return false;
        }
        return appointmentDao.updateStatus(appointmentId, "已就诊");
    }

    public boolean exportToFile(String filePath) {
        List<Appointment> appointments = appointmentDao.findAll();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("========== 医院预约记录导出 ==========");
            writer.println("导出时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("========================================");
            writer.printf("%-10s %-10s %-15s %-20s %-8s%n", "用户姓名", "医生姓名", "科室", "预约时间", "状态");
            writer.println("--------------------------------------------------------------");
            for (Appointment a : appointments) {
                writer.printf("%-10s %-10s %-15s %-20s %-8s%n",
                        a.getUserName(),
                        a.getDoctorName(),
                        a.getDeptName(),
                        a.getAppointTime().toLocalDate() + " " + a.getTimeSlot(),
                        a.getStatus());
            }
            writer.println("========================================");
            writer.println("共 " + appointments.size() + " 条记录");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

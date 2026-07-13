package com.hospital.entity;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int userId;
    private String userName;
    private int doctorId;
    private String doctorName;
    private String deptName;
    private LocalDateTime appointTime;
    private String timeSlot;
    private String status; // 待就诊, 已就诊, 已取消

    public Appointment() {}

    public Appointment(int userId, int doctorId, LocalDateTime appointTime, String timeSlot) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.appointTime = appointTime;
        this.timeSlot = timeSlot;
        this.status = "待就诊";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public LocalDateTime getAppointTime() { return appointTime; }
    public void setAppointTime(LocalDateTime appointTime) { this.appointTime = appointTime; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-10s %-8s %-15s %-20s %-8s %-8s",
                userName, doctorName, deptName, appointTime.toLocalDate() + " " + timeSlot, status, id);
    }
}

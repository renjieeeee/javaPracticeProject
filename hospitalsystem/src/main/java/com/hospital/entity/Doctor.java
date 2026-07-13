package com.hospital.entity;

public class Doctor {
    private int id;
    private String doctorCode;
    private String doctorName;
    private int deptId;
    private String deptName;
    private String title;
    private boolean status; // true=正常, false=停诊

    public Doctor() {}

    public Doctor(String doctorCode, String doctorName, int deptId, String title) {
        this.doctorCode = doctorCode;
        this.doctorName = doctorName;
        this.deptId = deptId;
        this.title = title;
        this.status = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDoctorCode() { return doctorCode; }
    public void setDoctorCode(String doctorCode) { this.doctorCode = doctorCode; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%-8s %-10s %-15s %-10s %-6s",
                doctorCode, doctorName, deptName != null ? deptName : "", title, status ? "正常" : "停诊");
    }
}

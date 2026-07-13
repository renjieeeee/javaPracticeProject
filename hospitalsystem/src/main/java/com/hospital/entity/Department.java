package com.hospital.entity;

public class Department {
    private int id;
    private String deptCode;
    private String deptName;

    public Department() {}

    public Department(String deptCode, String deptName) {
        this.deptCode = deptCode;
        this.deptName = deptName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    @Override
    public String toString() {
        return String.format("%-8s %-15s", deptCode, deptName);
    }
}

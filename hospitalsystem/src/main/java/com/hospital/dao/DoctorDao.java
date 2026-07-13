package com.hospital.dao;

import com.hospital.entity.Doctor;
import com.hospital.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {

    public List<Doctor> findAll() {
        String sql = "SELECT d.*, dept.dept_name FROM doctors d LEFT JOIN departments dept ON d.dept_id = dept.id ORDER BY d.doctor_code";
        List<Doctor> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public List<Doctor> findByDeptId(int deptId) {
        String sql = "SELECT d.*, dept.dept_name FROM doctors d LEFT JOIN departments dept ON d.dept_id = dept.id WHERE d.dept_id = ? AND d.status = 1";
        List<Doctor> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    public Doctor findById(int id) {
        String sql = "SELECT d.*, dept.dept_name FROM doctors d LEFT JOIN departments dept ON d.dept_id = dept.id WHERE d.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public Doctor findByCode(String code) {
        String sql = "SELECT d.*, dept.dept_name FROM doctors d LEFT JOIN departments dept ON d.dept_id = dept.id WHERE d.doctor_code = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public boolean insert(Doctor doctor) {
        String sql = "INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, doctor.getDoctorCode());
            ps.setString(2, doctor.getDoctorName());
            ps.setInt(3, doctor.getDeptId());
            ps.setString(4, doctor.getTitle());
            ps.setBoolean(5, doctor.isStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(Doctor doctor) {
        String sql = "UPDATE doctors SET doctor_name=?, dept_id=?, title=?, status=? WHERE doctor_code=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, doctor.getDoctorName());
            ps.setInt(2, doctor.getDeptId());
            ps.setString(3, doctor.getTitle());
            ps.setBoolean(4, doctor.isStatus());
            ps.setString(5, doctor.getDoctorCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean toggleStatus(int id) {
        String sql = "UPDATE doctors SET status = NOT status WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setDoctorCode(rs.getString("doctor_code"));
        doctor.setDoctorName(rs.getString("doctor_name"));
        doctor.setDeptId(rs.getInt("dept_id"));
        doctor.setDeptName(rs.getString("dept_name"));
        doctor.setTitle(rs.getString("title"));
        doctor.setStatus(rs.getBoolean("status"));
        return doctor;
    }
}

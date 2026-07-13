package com.hospital.dao;

import com.hospital.entity.Appointment;
import com.hospital.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {

    public List<Appointment> findByUserId(int userId) {
        String sql = "SELECT a.*, u.real_name AS user_name, d.doctor_name, dept.dept_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "JOIN departments dept ON d.dept_id = dept.id " +
                "WHERE a.user_id = ? ORDER BY a.appoint_time DESC";
        List<Appointment> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
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

    public List<Appointment> findAll() {
        String sql = "SELECT a.*, u.real_name AS user_name, d.doctor_name, dept.dept_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "JOIN departments dept ON d.dept_id = dept.id " +
                "ORDER BY a.appoint_time DESC";
        List<Appointment> list = new ArrayList<>();
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

    public boolean insert(Appointment appointment) {
        String sql = "INSERT INTO appointments(user_id, doctor_id, appoint_time, time_slot, status) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, appointment.getUserId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setTimestamp(3, Timestamp.valueOf(appointment.getAppointTime()));
            ps.setString(4, appointment.getTimeSlot());
            ps.setString(5, appointment.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public Appointment findById(int id) {
        String sql = "SELECT a.*, u.real_name AS user_name, d.doctor_name, dept.dept_name " +
                "FROM appointments a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "JOIN departments dept ON d.dept_id = dept.id " +
                "WHERE a.id = ?";
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

    private Appointment mapRow(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setUserId(rs.getInt("user_id"));
        appointment.setUserName(rs.getString("user_name"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setDoctorName(rs.getString("doctor_name"));
        appointment.setDeptName(rs.getString("dept_name"));
        appointment.setAppointTime(rs.getTimestamp("appoint_time").toLocalDateTime());
        appointment.setTimeSlot(rs.getString("time_slot"));
        appointment.setStatus(rs.getString("status"));
        return appointment;
    }
}

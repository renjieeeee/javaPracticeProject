package com.hospital.service;

import com.hospital.dao.DoctorDao;
import com.hospital.entity.Doctor;

import java.util.List;

public class DoctorService {
    private final DoctorDao doctorDao = new DoctorDao();

    public List<Doctor> findAll() {
        return doctorDao.findAll();
    }

    public List<Doctor> findByDeptId(int deptId) {
        return doctorDao.findByDeptId(deptId);
    }

    public Doctor findById(int id) {
        return doctorDao.findById(id);
    }

    public Doctor findByCode(String code) {
        return doctorDao.findByCode(code);
    }

    public boolean addDoctor(String code, String name, int deptId, String title) {
        if (doctorDao.findByCode(code) != null) {
            return false;
        }
        Doctor doctor = new Doctor(code, name, deptId, title);
        return doctorDao.insert(doctor);
    }

    public boolean updateDoctor(String code, String name, int deptId, String title) {
        Doctor doctor = doctorDao.findByCode(code);
        if (doctor == null) {
            return false;
        }
        doctor.setDoctorName(name);
        doctor.setDeptId(deptId);
        doctor.setTitle(title);
        return doctorDao.update(doctor);
    }

    public boolean toggleStatus(int id) {
        return doctorDao.toggleStatus(id);
    }
}

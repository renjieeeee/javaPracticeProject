package com.hospital.service;

import com.hospital.dao.DepartmentDao;
import com.hospital.entity.Department;

import java.util.List;

public class DepartmentService {
    private final DepartmentDao departmentDao = new DepartmentDao();

    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    public Department findByCode(String code) {
        return departmentDao.findByCode(code);
    }

    public boolean addDepartment(String code, String name) {
        if (departmentDao.findByCode(code) != null) {
            return false;
        }
        return departmentDao.insert(new Department(code, name));
    }

    public boolean updateDepartment(String code, String newName) {
        Department dept = departmentDao.findByCode(code);
        if (dept == null) {
            return false;
        }
        dept.setDeptName(newName);
        return departmentDao.update(dept);
    }

    public boolean deleteDepartment(String code) {
        return departmentDao.deleteByCode(code);
    }
}

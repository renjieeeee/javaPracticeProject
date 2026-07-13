-- 删除旧表（如果存在）
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS users;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hospital_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hospital_db;

-- 用户表
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 科室表
CREATE TABLE departments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dept_code VARCHAR(20) UNIQUE NOT NULL,
    dept_name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 医生表
CREATE TABLE doctors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_code VARCHAR(20) UNIQUE NOT NULL,
    doctor_name VARCHAR(50) NOT NULL,
    dept_id INT NOT NULL,
    title VARCHAR(20) NOT NULL,
    status TINYINT(1) DEFAULT 1,
    FOREIGN KEY (dept_id) REFERENCES departments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预约表
CREATE TABLE appointments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appoint_time DATETIME NOT NULL,
    time_slot VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT '待就诊',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员账号
INSERT INTO users(username, password, real_name, phone, role) VALUES('admin', 'admin123', '系统管理员', '13800000000', 'ADMIN');

-- 插入测试用户
INSERT INTO users(username, password, real_name, phone, role) VALUES('zhangsan', '123456', '张三', '13811111111', 'USER');

-- 插入科室数据
INSERT INTO departments(dept_code, dept_name) VALUES('D001', '内科');
INSERT INTO departments(dept_code, dept_name) VALUES('D002', '外科');
INSERT INTO departments(dept_code, dept_name) VALUES('D003', '儿科');
INSERT INTO departments(dept_code, dept_name) VALUES('D004', '妇产科');
INSERT INTO departments(dept_code, dept_name) VALUES('D005', '骨科');
INSERT INTO departments(dept_code, dept_name) VALUES('D006', '眼科');
INSERT INTO departments(dept_code, dept_name) VALUES('D007', '口腔科');
INSERT INTO departments(dept_code, dept_name) VALUES('D008', '皮肤科');

-- 插入医生数据
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC001', '王明', 1, '主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC002', '李华', 1, '副主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC003', '赵强', 2, '主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC004', '孙丽', 2, '主治医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC005', '周芳', 3, '副主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC006', '吴婷', 4, '主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC007', '郑伟', 5, '主治医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC008', '刘洋', 6, '住院医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC009', '陈静', 7, '副主任医师', 1);
INSERT INTO doctors(doctor_code, doctor_name, dept_id, title, status) VALUES('DOC010', '杨光', 8, '主治医师', 1);

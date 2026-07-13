package com.hospital.service;

import com.hospital.dao.UserDao;
import com.hospital.entity.User;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String password, String realName, String phone) {
        if (userDao.findByUsername(username) != null) {
            return false;
        }
        User user = new User(username, password, realName, phone, "USER");
        return userDao.insert(user);
    }

    public User findById(int id) {
        return userDao.findById(id);
    }
}

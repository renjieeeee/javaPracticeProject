package com.hospital.entity;

public class User {
    private int id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String role; // USER, ADMIN

    public User() {}

    public User(String username, String password, String realName, String phone, String role) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.phone = phone;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', realName='" + realName + "', role='" + role + "'}";
    }
}

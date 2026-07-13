package com.hospital.ui;

import com.hospital.entity.User;
import com.hospital.service.UserService;

import java.util.Scanner;

public class LoginUI {
    private final Scanner scanner;
    private final UserService userService;

    public LoginUI(Scanner scanner) {
        this.scanner = scanner;
        this.userService = new UserService();
    }

    public User show() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║     医院门诊预约挂号系统             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. 登录                             ║");
            System.out.println("║  2. 注册                             ║");
            System.out.println("║  0. 退出系统                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("请选择操作: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    User user = login();
                    if (user != null) return user;
                    break;
                case "2":
                    register();
                    break;
                case "0":
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                default:
                    System.out.println("无效选择，请重新输入");
            }
        }
    }

    private User login() {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();

        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("登录成功！欢迎, " + user.getRealName() + " (" + ("ADMIN".equals(user.getRole()) ? "管理员" : "普通用户") + ")");
            return user;
        } else {
            System.out.println("用户名或密码错误！");
            return null;
        }
    }

    private void register() {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        System.out.print("请输入真实姓名: ");
        String realName = scanner.nextLine().trim();
        System.out.print("请输入手机号: ");
        String phone = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
            System.out.println("用户名、密码和真实姓名不能为空！");
            return;
        }

        if (userService.register(username, password, realName, phone)) {
            System.out.println("注册成功！请登录。");
        } else {
            System.out.println("注册失败，用户名可能已存在！");
        }
    }
}

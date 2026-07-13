package com.hospital.gui;

import com.hospital.entity.User;
import com.hospital.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserService userService;

    public LoginFrame() {
        userService = new UserService();
        initUI();
    }

    private void initUI() {
        setTitle("医院门诊预约挂号系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("医院门诊预约挂号系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(new Color(70, 130, 180));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        usernameField = new JTextField(18);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("密  码:");
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton loginBtn = createButton("登  录", new Color(70, 130, 180));
        loginBtn.addActionListener(e -> doLogin());

        JButton registerBtn = createButton("注  册", new Color(100, 149, 237));
        registerBtn.addActionListener(e -> doRegister());

        JButton exitBtn = createButton("退  出", new Color(178, 34, 34));
        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(exitBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        passwordField.addActionListener(e -> doLogin());

        add(mainPanel);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userService.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "登录成功！欢迎, " + user.getRealName());
            dispose();
            if ("ADMIN".equals(user.getRole())) {
                new AdminFrame().setVisible(true);
            } else {
                new UserFrame(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void doRegister() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField regUsername = new JTextField();
        JPasswordField regPassword = new JPasswordField();
        JTextField regRealName = new JTextField();
        JTextField regPhone = new JTextField();

        panel.add(new JLabel("用户名:"));
        panel.add(regUsername);
        panel.add(new JLabel("密码:"));
        panel.add(regPassword);
        panel.add(new JLabel("真实姓名:"));
        panel.add(regRealName);
        panel.add(new JLabel("手机号:"));
        panel.add(regPhone);

        int result = JOptionPane.showConfirmDialog(this, panel, "用户注册", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = regUsername.getText().trim();
            String password = new String(regPassword.getPassword()).trim();
            String realName = regRealName.getText().trim();
            String phone = regPhone.getText().trim();

            if (username.isEmpty() || password.isEmpty() || realName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名、密码和真实姓名不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (userService.register(username, password, realName, phone)) {
                JOptionPane.showMessageDialog(this, "注册成功！请登录。");
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，用户名可能已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

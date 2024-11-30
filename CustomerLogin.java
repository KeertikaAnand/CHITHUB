package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerLogin extends JFrame {
    private JTextField customerIdField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public CustomerLogin() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Login Form");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setResizable(true);

        // Create a JLabel with ImageIcon as the background
        ImageIcon backgroundImage = new ImageIcon("D:\\java pic\\cuslogin.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null); // Set layout to null for custom positioning

        Font font = new Font("Bookman Old Style", Font.PLAIN, 15);
        Font labelFont = new Font("Times new roman", Font.PLAIN, 20);

        // Welcome label


        // Customer ID label and field
        JLabel l1 = new JLabel("Customer Id");
        l1.setFont(labelFont);
        l1.setBounds(100, 330, 300, 35);
        l1.setForeground(Color.BLACK);
        backgroundLabel.add(l1);

        customerIdField = new JTextField();
        customerIdField.setBounds(100, 365, 350, 35);
        customerIdField.setFont(font);
        backgroundLabel.add(customerIdField);

        // Password label and field
        JLabel l2 = new JLabel("Password");
        l2.setFont(labelFont);
        l2.setBounds(100, 430, 200, 100);
        l2.setForeground(Color.BLACK);
        backgroundLabel.add(l2);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 500, 350, 35);
        backgroundLabel.add(passwordField);

        // Status label for login feedback
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 10));
        statusLabel.setBounds(100, 570, 500, 70);
        statusLabel.setForeground(Color.RED);
        backgroundLabel.add(statusLabel);

        // Login button
        JButton loginButton = new JButton(new ImageIcon("D:\\login.jpg"));
        loginButton.setBounds(250, 650, 165, 48);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText();
                String password = new String(passwordField.getPassword());

                if (validateLogin(customerId, password)) {
                    JOptionPane.showMessageDialog(CustomerLogin.this, "Login Successful!");
                    // Log the login time
                    logLoginTime(customerId);
                    //JOptionPane.showMessageDialog(CustomerLogin.this, "Login Successful!");
                    openJoinChitWindow(customerId);
                } else {
                    statusLabel.setText("Invalid Customer ID or Password. Please try again.");
                    customerIdField.setText("");
                    passwordField.setText("");
                }
            }
        });
        backgroundLabel.add(loginButton);

        getContentPane().add(backgroundLabel);
    }

    private void logLoginTime(String customerId) {
        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "INSERT INTO customer_logins (customer_id, login_time) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, customerId);
                preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to log login time.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateLogin(String customerId, String password) {
        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "SELECT * FROM customers WHERE customer_id = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, customerId);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    DatabaseConnector.closeConnection(connection);
                    return true;
                }
                DatabaseConnector.closeConnection(connection);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void openJoinChitWindow(String customerId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JoinChit(customerId).setVisible(true);
            }
        });
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomerLogin().setVisible(true);
            }
        });
    }
}
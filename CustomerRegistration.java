package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Pattern;

public class CustomerRegistration extends JFrame {
    private JTextField customerNameField;
    private JTextField aadharNoField;
    private JTextField panNoField;
    private JTextField addressField;
    private JTextField phoneNumField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField bankAccountNoField;
    private JButton registerButton;
    private JLabel loginLink;
    private JPanel formPanel; // Declare formPanel as a class member

    private JFrame f; // Declare f as a field of the class

    public CustomerRegistration() {
        f = new JFrame("Customer Registration"); // Initialize f here
        // Create a JLabel with ImageIcon as the background
        ImageIcon backgroundImage = new ImageIcon("D:\\java pic\\Register.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);

        // Create components
        // Set color to white

        // Set layout to null for custom positioning
        backgroundLabel.setLayout(null);
        Font labelFont = new Font("times new roman", Font.PLAIN, 20);
        Font font = new Font("times new roman", Font.PLAIN, 15);

        JLabel lblCustomerName = new JLabel("Name");
        lblCustomerName.setFont(labelFont);
        lblCustomerName.setForeground(Color.GRAY);
        lblCustomerName.setBounds(700, 150, 300, 50);
        backgroundLabel.add(lblCustomerName);

        customerNameField = new JTextField();
        customerNameField.setBounds(810, 150, 300, 30);
        backgroundLabel.add(customerNameField);
        customerNameField.setFont(font);

        JLabel lblAadharNo = new JLabel("Aadhar No");
        lblAadharNo.setFont(labelFont);
        lblAadharNo.setForeground(Color.GRAY);
        lblAadharNo.setBounds(700, 210, 300, 50);
        backgroundLabel.add(lblAadharNo);

        aadharNoField = new JTextField();
        aadharNoField.setBounds(810, 210, 300, 30);
        backgroundLabel.add(aadharNoField);
        aadharNoField.setFont(font);

        JLabel lblPanNo = new JLabel("PAN No");
        lblPanNo.setFont(labelFont);
        lblPanNo.setForeground(Color.GRAY);
        lblPanNo.setBounds(700, 270, 250, 40);
        backgroundLabel.add(lblPanNo);

        panNoField = new JTextField();
        panNoField.setBounds(810, 270, 300, 30);
        backgroundLabel.add(panNoField);
        panNoField.setFont(font);

        JLabel lblAddress = new JLabel("Address");
        lblAddress.setFont(labelFont);
        lblAddress.setForeground(Color.GRAY);
        lblAddress.setBounds(700, 330, 250, 40);
        backgroundLabel.add(lblAddress);

        addressField = new JTextField();
        addressField.setBounds(810, 330, 300, 30);
        backgroundLabel.add(addressField);
        addressField.setFont(font);

        JLabel lblPhoneNum = new JLabel("Phone No");
        lblPhoneNum.setFont(labelFont);
        lblPhoneNum.setForeground(Color.GRAY);
        lblPhoneNum.setBounds(700, 390, 350, 40);
        backgroundLabel.add(lblPhoneNum);

        phoneNumField = new JTextField();
        phoneNumField.setBounds(810, 390, 300, 30);
        backgroundLabel.add(phoneNumField);
        phoneNumField.setFont(font);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(Color.GRAY);
        lblEmail.setBounds(700, 450, 250, 40);
        backgroundLabel.add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(810, 450, 300, 30);
        backgroundLabel.add(emailField);
        emailField.setFont(font);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(labelFont);
        lblPassword.setForeground(Color.GRAY);
        lblPassword.setBounds(700, 510, 250, 40);
        backgroundLabel.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(810, 510, 300, 30);
        backgroundLabel.add(passwordField);
        passwordField.setFont(font);

        JLabel lblBankAccountNo = new JLabel("Acc.No");
        lblBankAccountNo.setFont(labelFont);
        lblBankAccountNo.setForeground(Color.GRAY);
        lblBankAccountNo.setBounds(700, 570, 350, 40);
        backgroundLabel.add(lblBankAccountNo);

        bankAccountNoField = new JTextField();
        bankAccountNoField.setBounds(810, 570, 300, 30);
        backgroundLabel.add(bankAccountNoField);
        bankAccountNoField.setFont(font);

        registerButton = new RButton("Register", new Color(50, 50, 255, 150)); // Using custom button class
        registerButton.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 25)); // Adjust font size
        registerButton.setBounds(830, 670, 200, 50); // Adjusted size and position
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (areAllTextFieldsFilled()) {
                    if (validateFields()) {
                    registerCustomer();
                } }else {
                    JOptionPane.showMessageDialog(CustomerRegistration.this, "Please fill out all the text fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        backgroundLabel.add(registerButton);

        // Convert the "Already registered? Login here" JLabel to a JButton with RButton design
        RButton loginButton = new RButton("Already registered? Login here", new Color(255, 255, 255, 150));
        loginButton.setFont(new Font("Times New Roman", Font.PLAIN, 15)); // Adjust font size

        loginButton.setForeground(Color.red); // Set text color to blue
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand when hovering
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose(); // Close the registration page
                new CustomerLogin().setVisible(true); // Open the login page
            }
        });

        // Create a panel for the login button
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centered panel for login link
        loginPanel.add(loginButton); // Add login button to login panel
        loginPanel.setBounds(830, 750, 190, 25); // Adjusted position and size
        backgroundLabel.add(loginPanel);

        // Add backgroundLabel to the content pane of the frame
        f.getContentPane().add(backgroundLabel);

        // Set frame size to match background image
        f.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Add the "Welcome Customer" label


        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true); // Disabled resizing
        f.setVisible(true);
    }

    // Method to check if all text fields are filled
    private boolean areAllTextFieldsFilled() {
        return !customerNameField.getText().isEmpty() &&
                !aadharNoField.getText().isEmpty() &&
                !panNoField.getText().isEmpty() &&
                !addressField.getText().isEmpty() &&
                !phoneNumField.getText().isEmpty() &&
                !emailField.getText().isEmpty() &&
                passwordField.getPassword().length > 0 &&
                !bankAccountNoField.getText().isEmpty();
    }
    // Method to validate all fields
    private boolean validateFields() {
        if (!validateAadharNumber(aadharNoField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid Aadhar number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validatePanNumber(panNoField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid PAN number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validatePhoneNumber(phoneNumField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validateEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!validateBankAccountNumber(bankAccountNoField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid bank account number.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private boolean validateAadharNumber(String aadhar) {
        return aadhar.matches("\\d{12}");
    }

    private boolean validatePanNumber(String pan) {
        return pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }

    private boolean validatePhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    private boolean validateBankAccountNumber(String account) {
        return account.matches("\\d{11}");
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

private void registerCustomer() {
    // Retrieving values from input fields
    String customerName = customerNameField.getText();
    String aadharStr = aadharNoField.getText();
    String panNo = panNoField.getText();
    String address = addressField.getText();
    String phoneNumStr = phoneNumField.getText();
    String email = emailField.getText();
    char[] passwordChars = passwordField.getPassword();
    String password = new String(passwordChars);
    String bankAccountNoStr = bankAccountNoField.getText();
    String customerId = generateCustomerId();

    // Validate Aadhar number
    if (!aadharStr.matches("\\d{12}")) {
        JOptionPane.showMessageDialog(this, "Aadhar number must be 12 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    long aadharNo = Long.parseLong(aadharStr);

    // Validate PAN number
    if (!panNo.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
        JOptionPane.showMessageDialog(this, "Invalid PAN number format.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validate phone number
    if (!phoneNumStr.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this, "Phone number must be 10 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    long phoneNum = Long.parseLong(phoneNumStr);

    // Validate bank account number
    if (!bankAccountNoStr.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Invalid bank account number format.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    long bankAccountNo = Long.parseLong(bankAccountNoStr);

    // Register the customer in the database
    try {
        Connection connection = DatabaseConnector.connect();
        if (connection != null) {
            String query = "INSERT INTO customers (customer_id, customer_name, aadhar_no, pan_no, address, phone_num, email, password, bank_account_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerId);
            preparedStatement.setString(2, customerName);
            preparedStatement.setLong(3, aadharNo);
            preparedStatement.setString(4, panNo);
            preparedStatement.setString(5, address);
            preparedStatement.setLong(6, phoneNum);
            preparedStatement.setString(7, email);
            preparedStatement.setString(8, password);
            preparedStatement.setLong(9, bankAccountNo);
            preparedStatement.executeUpdate();
            DatabaseConnector.closeConnection(connection);

            JOptionPane.showMessageDialog(this, "Registration successful! Customer ID: " + customerId, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear input fields after successful registration
            clearFields();
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void clearFields() {
        customerNameField.setText("");
        aadharNoField.setText("");
        panNoField.setText("");
        addressField.setText("");
        phoneNumField.setText("");
        emailField.setText("");
        passwordField.setText("");
        bankAccountNoField.setText("");
    }


    private String generateCustomerId() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Custom JButton class with transparent background and curved ends
    class RButton extends JButton {
        private Color backgroundColor;

        public RButton(String text, Color backgroundColor) {
            super(text);
            this.backgroundColor = backgroundColor;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false); // Disable default button border
            setForeground(Color.black); // Set text color to black
            setFocusPainted(false); // Remove focus border
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Draw rounded rectangle
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerRegistration();
            }
        });
    }
}
package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends Component {
    private JFrame f; // Declare f as a field of the class

    public AdminLogin() {
        f = new JFrame("LOGIN"); // Initialize f here

        // Create a JLabel with ImageIcon as the background
        ImageIcon backgroundImage = new ImageIcon("D:\\background.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(null);

        // Create components
        JLabel l1, l2;
        JLabel lWelcome;
        JTextField tf1;
        JPasswordField tf2;
        JLabel statusLabel = new JLabel();
        statusLabel.setBounds(600, 410, 400, 30);
        Font font = new Font("Bookman Old Style", Font.PLAIN, 24);

        lWelcome = new JLabel("ADMIN LOGIN");
        lWelcome.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 50));
        lWelcome.setBounds(600, 125, 500, 100);
        lWelcome.setForeground(Color.BLACK);

        l1 = new JLabel("ADMIN ID");
        l1.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 30));
        l1.setBounds(480, 230, 300, 100);
        l1.setForeground(Color.BLACK);
        tf1 = new JTextField();
        tf1.setBounds(700, 265, 350, 40);
        tf1.setFont(font);

        l2 = new JLabel("PASSWORD");
        l2.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 30));
        l2.setBounds(420, 330, 300, 100);
        l2.setForeground(Color.BLACK);
        tf2 = new JPasswordField();
        tf2.setBounds(700, 365, 350, 40);

        // Add components to backgroundLabel instead of JFrame directly
        backgroundLabel.add(l1);
        backgroundLabel.add(l2);
        backgroundLabel.add(tf1);
        backgroundLabel.add(tf2);

        JButton login = new JButton(new ImageIcon("D:\\java pic\\adlog.jpg"));
        login.setBounds(700, 500, 160, 70);
        backgroundLabel.add(login);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tf1.getText();
                String password = new String(tf2.getPassword());
                if (validateAdmin(username, password)) {
                    openAdminPanel();
                } else {
                    statusLabel.setText("Invalid username or password"); // Set the error message
                    statusLabel.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 20));
                    statusLabel.setBounds(600, 420, 500, 70);
                    statusLabel.setForeground(Color.BLACK); // Optionally set the color
                }
            }
        });

        // Add components to backgroundLabel instead of JFrame directly
        backgroundLabel.add(lWelcome); // Add the "Welcome Admin" label

        // Add backgroundLabel to the content pane of the frame
        f.getContentPane().add(backgroundLabel);

        // Set frame size to match background image
        f.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Add the status label to the backgroundLabel
        backgroundLabel.add(statusLabel);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true); // resizing
        f.setVisible(true);
    }

    private boolean validateAdmin(String username, String password) {
        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "SELECT * FROM admin WHERE admin_name = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                boolean isValid = resultSet.next();
                DatabaseConnector.closeConnection(connection);
                return isValid;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void openAdminPanel() {
        // Create an instance of AdminPanel
        AdminPanel adminPanel = new AdminPanel();
        // Make the AdminPanel visible
        adminPanel.setVisible(true);
        // Dispose of the current AdminLogin frame
        f.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminLogin();
            }
        });
    }
}
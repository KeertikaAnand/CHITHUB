
package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class ManageChitGroups extends JFrame {
    private JButton addButton;
    private JTable chitGroupTable;
    private DefaultTableModel tableModel;

    public ManageChitGroups() {
        initializeComponents();
        loadChitGroupsFromDatabase();
    }

    private void initializeComponents() {
        setTitle("Manage Chit Groups");
        setSize(1900, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a custom panel with a background image
        ViewChit.BackgroundPanel backgroundPanel = new ViewChit.BackgroundPanel(new ImageIcon("D:\\java pic\\vc1.jpg").getImage());
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel); // Set the content pane to the background panel

        // Label for "Manage Chit Groups"
        JLabel titleLabel = new JLabel("Manage Chit Groups", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel for the table with a background image
        JPanel tablePanel = new ViewChit.BackgroundPanel(new ImageIcon("C:\\Users\\USER\\Downloads\\output-onlinepngtools (3).png").getImage());
        tablePanel.setLayout(new BorderLayout());
        backgroundPanel.add(tablePanel, BorderLayout.CENTER);

        // Table to display chit groups
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        chitGroupTable = new JTable(tableModel);
        chitGroupTable.setFont(new Font("Bookman Old Style", Font.PLAIN, 18));
        chitGroupTable.setRowHeight(30);
        chitGroupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chitGroupTable.setAutoCreateRowSorter(true);

        JTableHeader header = chitGroupTable.getTableHeader();
        header.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 20));
        header.setBackground(new Color(25, 20, 58));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chitGroupTable);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button to add new chit group
        addButton = new JButton("Add Chit Group");
        addButton.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 18));
        addButton.setBackground(new Color(25, 20, 58));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(200, 50));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addChitGroup();
            }
        });
        tablePanel.add(addButton, BorderLayout.SOUTH);

        // Add ListSelectionListener to the table
        chitGroupTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = chitGroupTable.getSelectedRow();
                if (selectedRow != -1) {
                    String groupId = (String) tableModel.getValueAt(selectedRow, 0);
                    new DisplayCustomerDetails(groupId);
                }
            }
        });
    }

    private void loadChitGroupsFromDatabase() {
        // Clear existing rows from the table
        tableModel.setRowCount(0);

        tableModel.setColumnIdentifiers(new String[]{"Group ID", "Name", "Scheme Amount", "Duration", "Monthly Due", "Available Slots"});

        try (Connection connection = DatabaseConnector.connect()) {
            if (connection != null) {
                String query = "SELECT * FROM chit_groups ORDER BY scheme_amount";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        String groupId = resultSet.getString("id"); // Change to String for Group ID
                        String name = resultSet.getString("name");
                        double schemeAmount = resultSet.getDouble("scheme_amount");
                        int duration = resultSet.getInt("duration");
                        double monthlyDue = resultSet.getDouble("monthly_due");
                        int availableSlots = resultSet.getInt("available_slots"); // Retrieve available slots from the database

                        tableModel.addRow(new Object[]{groupId, name, schemeAmount, duration, monthlyDue, availableSlots});
                    }
                }
                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void addChitGroup() {
        // Create a dialog for adding a new chit group
        JFrame addDialog = new JFrame("Add Chit Group");
        addDialog.setSize(400, 250);
        addDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addDialog.setLocationRelativeTo(null); // Center the dialog on the screen

        // Panel for the dialog content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        // Components for input fields
        JTextField nameField = new JTextField();
        JTextField schemeAmountField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField monthlyDueField = new JTextField();
        JTextField totalMembersField = new JTextField();

        // Labels for input fields
        JLabel nameLabel = new JLabel("Name:");
        JLabel schemeAmountLabel = new JLabel("Scheme Amount:");
        JLabel durationLabel = new JLabel("Tenure (months):");
        JLabel monthlyDueLabel = new JLabel("Monthly Due:");
        JLabel totalMembersLabel = new JLabel("Total Members:");

        // Set label font and color
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        nameLabel.setFont(labelFont);
        schemeAmountLabel.setFont(labelFont);
        durationLabel.setFont(labelFont);
        monthlyDueLabel.setFont(labelFont);
        totalMembersLabel.setFont(labelFont);
        nameLabel.setForeground(Color.BLACK);
        schemeAmountLabel.setForeground(Color.BLACK);
        durationLabel.setForeground(Color.BLACK);
        monthlyDueLabel.setForeground(Color.BLACK);
        totalMembersLabel.setForeground(Color.BLACK);

        // Add components to input panel
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(schemeAmountLabel);
        inputPanel.add(schemeAmountField);
        inputPanel.add(durationLabel);
        inputPanel.add(durationField);
        inputPanel.add(monthlyDueLabel);
        inputPanel.add(monthlyDueField);
        inputPanel.add(totalMembersLabel);
        inputPanel.add(totalMembersField);

        // Button to submit the form
        JButton submitButton = new JButton("Add");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(59, 89, 182));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> {
            // Get values from input fields
            String name = nameField.getText();
            String schemeAmountText = schemeAmountField.getText();
            String durationText = durationField.getText();
            String monthlyDueText = monthlyDueField.getText();
            String totalMembersText = totalMembersField.getText();

            // Check if any of the fields are empty
            if (name.isEmpty() || schemeAmountText.isEmpty() || durationText.isEmpty() || monthlyDueText.isEmpty() || totalMembersText.isEmpty()) {
                JOptionPane.showMessageDialog(addDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse numerical values
            double schemeAmount;
            int duration;
            double monthlyDue;
            int totalMembers;
            try {
                schemeAmount = Double.parseDouble(schemeAmountText);
                duration = Integer.parseInt(durationText);
                monthlyDue = Double.parseDouble(monthlyDueText);
                totalMembers = Integer.parseInt(totalMembersText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addDialog, "Invalid numerical format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call method to add the chit group to the database
            addChitGroupToDatabase(name, schemeAmount, duration, monthlyDue, totalMembers);

            // Close the dialog
            addDialog.dispose();

            // Refresh the table to show the newly added chit group
            loadChitGroupsFromDatabase();
        });

        // Panel for the submit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);

        // Add input panel and button panel to the main panel
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Set background color
        panel.setBackground(new Color(25, 20, 58));

        // Add panel to the dialog
        addDialog.add(panel);
        addDialog.setVisible(true);
    }


    private void addChitGroupToDatabase(String name, double schemeAmount, int duration, double monthlyDue, int totalMembers) {
        try (Connection connection = DatabaseConnector.connect()) {
            if (connection != null) {
                String groupId = generateChitGroupId(); // Generate Group ID
                String query = "INSERT INTO chit_groups (id, name, scheme_amount, duration, monthly_due, total_members, available_slots) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, groupId);
                    preparedStatement.setString(2, name);
                    preparedStatement.setDouble(3, schemeAmount);
                    preparedStatement.setInt(4, duration);
                    preparedStatement.setDouble(5, monthlyDue);
                    preparedStatement.setInt(6, totalMembers);
                    preparedStatement.setInt(7, totalMembers); // Set available slots initially equal to total members
                    preparedStatement.executeUpdate();
                }
                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to generate a random alphanumeric ID of length 6 characters
    private String generateChitGroupId() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }


    public class DisplayCustomerDetails extends JFrame {
        public DisplayCustomerDetails(String groupId) {
            setTitle("Customer Details in Chit Group " + groupId);
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null); // Center the dialog on the screen

            // Create a panel with a background picture
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon imageIcon = new ImageIcon("D:\\java pic\\adminbg.jpeg"); // Replace with your image path
                    Image image = imageIcon.getImage();
                    if (image != null) {
                        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        System.err.println("Failed to load background image");
                    }
                }
            };
            backgroundPanel.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false); // Set panel transparent

            // Table to display customer details
            DefaultTableModel customerTableModel = new DefaultTableModel();
            JTable customerTable = new JTable(customerTableModel);
            customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
            customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            customerTable.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(customerTable) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (getViewport().getView() instanceof JTable) {
                        JTable table = (JTable) getViewport().getView();
                        if (table.getRowCount() == 0 && !isOpaque()) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setColor(Color.WHITE); // Change the text color
                            g2.setFont(new Font("Arial", Font.BOLD, 20));
                            String message = "No data available";
                            FontMetrics fm = g2.getFontMetrics();
                            int x = (getWidth() - fm.stringWidth(message)) / 2;
                            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                            g2.drawString(message, x, y);
                            g2.dispose();
                        }
                    }
                }
            };

            panel.add(scrollPane, BorderLayout.CENTER);

            customerTableModel.setColumnIdentifiers(new String[]{"Customer ID", "Name", "Address", "Phone No", "Email"});
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chit_fund", "root", "12345")) {
                if (connection != null) {
                    String query = "SELECT c.customer_id, c.customer_name, c.address, c.phone_num, c.email " +
                            "FROM customers c " +
                            "INNER JOIN customer_chit_groups ccg ON c.customer_id = ccg.customer_id " +
                            "WHERE ccg.chit_group_id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, groupId);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            while (resultSet.next()) {
                                String customerId = resultSet.getString("customer_id");
                                String customerName = resultSet.getString("customer_name");
                                String address = resultSet.getString("address");
                                String phoneNum = resultSet.getString("phone_num");
                                String email = resultSet.getString("email");
                                customerTableModel.addRow(new Object[]{customerId, customerName, address, phoneNum, email});
                            }
                        }
                    }
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Add panel to a styled container
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(new Color(45, 0, 0, 0)); // Set container background transparent
            container.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            container.add(panel, BorderLayout.CENTER);

            backgroundPanel.add(container, BorderLayout.CENTER);

            setContentPane(backgroundPanel);
            setVisible(true);
        }

        public void main(String[] args) {
            SwingUtilities.invokeLater(() -> new DisplayCustomerDetails("your_group_id"));
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageChitGroups().setVisible(true));
    }
}

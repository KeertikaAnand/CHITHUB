package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageCustomers extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public ManageCustomers() {
        setTitle("Manage Customers");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        initializeComponents();
        addCustomerSelectionListener(); // Add listener for row selection
        setVisible(true);
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        setContentPane(panel); // Set the content pane to the panel

        // Create a custom panel with a background image
        ViewChit.BackgroundPanel backgroundPanel = new ViewChit.BackgroundPanel(new ImageIcon("D:\\java pic\\vc1.jpg").getImage());
        backgroundPanel.setLayout(new BorderLayout());
        panel.add(backgroundPanel, BorderLayout.CENTER); // Add the background panel to the main panel

        // Label for "Customer Details"
        JLabel titleLabel = new JLabel("Customer Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Table to display customer information
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("times new roman", Font.PLAIN, 15));
        customerTable.setRowHeight(30);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setAutoCreateRowSorter(true);

        JTableHeader header = customerTable.getTableHeader();
        header.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 20));
        header.setBackground(new Color(25, 20, 58));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        loadCustomersFromDatabase();
    }

    private void loadCustomersFromDatabase() {
        tableModel.setColumnIdentifiers(new String[]{"Customer ID", "Customer Name", "Aadhar No", "PAN No", "Address", "Phone Number", "Email", "Bank Account No"});

        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "SELECT * FROM customers";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        String customerId = resultSet.getString("customer_id");
                        String customerName = resultSet.getString("customer_name");
                        String aadharNo = resultSet.getString("aadhar_no");
                        String panNo = resultSet.getString("pan_no");
                        String address = resultSet.getString("address");
                        String phoneNum = resultSet.getString("phone_num");
                        String email = resultSet.getString("email");
                        String bankAccountNo = resultSet.getString("bank_account_no");

                        tableModel.addRow(new Object[]{customerId, customerName, aadharNo, panNo, address, phoneNum, email, bankAccountNo});
                    }
                }
                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customers from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add listener for row selection
    private void addCustomerSelectionListener() {
        customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = customerTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String customerId = (String) tableModel.getValueAt(selectedRow, 0);
                        displayChitGroupsForCustomer(customerId);
                    }
                }
            }
        });
    }

    // Method to display chit groups for a customer
    private void displayChitGroupsForCustomer(String customerId) {
        JFrame chitGroupsDialog = new JFrame("Chit Groups for Customer " + customerId);
        chitGroupsDialog.setSize(800, 600);
        chitGroupsDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chitGroupsDialog.setLocationRelativeTo(this); // Center the dialog relative to this frame

        JPanel panel = new JPanel(new BorderLayout());

        // Table to display chit groups
        DefaultTableModel chitGroupsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        JTable chitGroupsTable = new JTable(chitGroupsTableModel);
        JScrollPane scrollPane = new JScrollPane(chitGroupsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        chitGroupsTableModel.setColumnIdentifiers(new String[]{"Group ID", "Name", "Scheme Amount", "Duration", "Monthly Due", "Available Slots"});

        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "SELECT cg.id, cg.name, cg.scheme_amount, cg.duration, cg.monthly_due, cg.available_slots " +
                        "FROM chit_groups cg " +
                        "INNER JOIN customer_chit_groups ccg ON cg.id = ccg.chit_group_id " +
                        "WHERE ccg.customer_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, customerId);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            String groupId = resultSet.getString("id");
                            String name = resultSet.getString("name");
                            double schemeAmount = resultSet.getDouble("scheme_amount");
                            int duration = resultSet.getInt("duration");
                            double monthlyDue = resultSet.getDouble("monthly_due");
                            int availableSlots = resultSet.getInt("available_slots");

                            chitGroupsTableModel.addRow(new Object[]{groupId, name, schemeAmount, duration, monthlyDue, availableSlots});
                        }
                    }
                }
                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load chit groups for the customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        chitGroupsDialog.add(panel);
        chitGroupsDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManageCustomers();
            }
        });
    }
}

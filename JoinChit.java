package chitFund.gui;

import chitFund.util.DatabaseConnector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinChit extends JFrame {
    private String customerId;
    private JTable chitGroupTable;
    private DefaultTableModel tableModel;

    public JoinChit(String customerId) {
        this.customerId = customerId;
        initializeComponents();
        loadChitGroupsFromDatabase();
    }

    private void initializeComponents() {
        setTitle("Join Chit Group");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create a custom panel with a background image
        BackgroundPanel backgroundPanel = new BackgroundPanel(new ImageIcon("D:\\java pic\\vc1.jpg").getImage());
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel); // Set the content pane to the background panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Existing Chit Groups");
        label.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 34));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE); // Ensure the label text is visible
        backgroundPanel.add(label, gbc);

        // Custom panel with a different background image for the table area
        BackgroundPanel tableBackgroundPanel = new BackgroundPanel(new ImageIcon("C:\\Users\\USER\\Downloads\\output-onlinepngtools (2).png").getImage());
        tableBackgroundPanel.setLayout(new BorderLayout());

        // Table to display chit groups
        // Table to display chit groups
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        chitGroupTable = new JTable(tableModel);
        chitGroupTable.setFont(new Font("Bookman Old Style", Font.PLAIN, 18));
        chitGroupTable.setRowHeight(30);
        chitGroupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chitGroupTable.setAutoCreateRowSorter(true);

        JTableHeader header = chitGroupTable.getTableHeader();
        header.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 20));
        header.setBackground(new Color(175, 50, 237));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(chitGroupTable);
        scrollPane.getViewport().setOpaque(false); // Make the viewport transparent
        scrollPane.setOpaque(false); // Make the scroll pane transparent
        tableBackgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Customize table header renderer for better appearance
        TableCellRenderer headerRenderer = chitGroupTable.getTableHeader().getDefaultRenderer();
        chitGroupTable.getTableHeader().setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
            Component comp = headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            comp.setBackground(new Color(76, 0, 153));
            comp.setForeground(Color.WHITE);
            return comp;
        });

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        backgroundPanel.add(tableBackgroundPanel, gbc);

        // View My Chit Groups button
        JButton viewChitButton = new JButton("View My Chit Groups");
        viewChitButton.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 18));
        viewChitButton.setBackground(new Color(25, 20, 58));
        viewChitButton.setForeground(Color.WHITE);
        viewChitButton.setFocusPainted(false);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        backgroundPanel.add(viewChitButton, gbc);
        viewChitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewChit viewChit = new ViewChit(customerId);
                viewChit.setVisible(true);
            }
        });

        // Add ListSelectionListener to the table
        chitGroupTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = chitGroupTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String groupId = (String) tableModel.getValueAt(selectedRow, 0);
                        String groupName = (String) tableModel.getValueAt(selectedRow, 1);
                        showConfirmationDialog(groupId, groupName);
                    }
                }
            }
        });
    }

    private void loadChitGroupsFromDatabase() {
        // Clear existing rows from the table
        tableModel.setRowCount(0);

        tableModel.setColumnIdentifiers(new String[]{"Group ID", "Name", "Scheme Amount", "Duration", "Monthly Due", "Total Members", "Available Slots"});

        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                String query = "SELECT id, name, scheme_amount, duration, monthly_due, total_members, available_slots FROM chit_groups";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String groupId = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    double schemeAmount = resultSet.getDouble("scheme_amount");
                    int duration = resultSet.getInt("duration");
                    double monthlyDue = resultSet.getDouble("monthly_due");
                    int totalMembers = resultSet.getInt("total_members");
                    int availableSlots = resultSet.getInt("available_slots");

                    // Only add chit groups with available slots
                    if (availableSlots > 0) {
                        Object[] rowData = {groupId, name, schemeAmount, duration, monthlyDue, totalMembers, availableSlots};
                        tableModel.addRow(rowData);
                    }
                }

                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showConfirmationDialog(String groupId, String groupName) {
        int option = JOptionPane.showConfirmDialog(this, "Join this chit group \"" + groupName + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Perform the action to join the chit group
            joinChitGroup(groupId);
        }
    }

    private void joinChitGroup(String groupId) {
        // Perform the action to join the chit group
        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                // Check if the user is already a member of the chit group
                if (isMemberOfChitGroup(connection, customerId, groupId)) {
                    JOptionPane.showMessageDialog(this, "You are already a member of this chit group.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Add the user to the chit group
                String query = "INSERT INTO customer_chit_groups (customer_id, chit_group_id) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, customerId);
                preparedStatement.setString(2, groupId);
                preparedStatement.executeUpdate();

                // Update available slots in chit_groups table
                String updateQuery = "UPDATE chit_groups SET available_slots = available_slots - 1 WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, groupId);
                updateStatement.executeUpdate();

                DatabaseConnector.closeConnection(connection);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Provide feedback to the user
        JOptionPane.showMessageDialog(this, "You have successfully joined the chit group.", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadChitGroupsFromDatabase(); // Refresh the table data after joining a chit group
    }

    private boolean isMemberOfChitGroup(Connection connection, String customerId, String groupId) throws SQLException {
        String query = "SELECT * FROM customer_chit_groups WHERE customer_id = ? AND chit_group_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, customerId);
        preparedStatement.setString(2, groupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    // Custom JPanel to draw background image
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
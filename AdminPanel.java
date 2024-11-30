package chitFund.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    public AdminPanel() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // Prevent resizing

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("D:\\panel.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);

        // Set layout to null for custom positioning
        backgroundLabel.setLayout(null);

        // Create panel for buttons
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Make panel transparent
        panel.setLayout(new GridLayout(3, 1, 80, 70)); // Use GridLayout for horizontal arrangement with gaps

        // Create buttons using the custom RButton class
        JButton manageChitGroupsButton = new RButton("Manage Chit Groups", new Color(255, 255, 255, 150));
        manageChitGroupsButton.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 30));
        JButton manageCustomersButton = new RButton("Manage Customers", new Color(255, 255, 255, 150));
        manageCustomersButton.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 30));
        JButton manageAuctionsButton = new RButton("Manage Auctions", new Color(255, 255, 255, 150));
        manageAuctionsButton.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 30));
        // Set preferred size for buttons (adjust as needed)
        Dimension buttonSize = new Dimension(400, 70);
        manageChitGroupsButton.setPreferredSize(buttonSize);
        manageCustomersButton.setPreferredSize(buttonSize);
        manageAuctionsButton.setPreferredSize(buttonSize);

        // Add buttons to the panel
        panel.add(manageChitGroupsButton);
        panel.add(manageCustomersButton);
        panel.add(manageAuctionsButton);

        // Position the panel in the center of the frame
        panel.setBounds((backgroundImage.getIconWidth() - panel.getPreferredSize().width) / 2,
                (backgroundImage.getIconHeight() - panel.getPreferredSize().height) / 2,
                panel.getPreferredSize().width,
                panel.getPreferredSize().height);

        // Add the panel to the backgroundLabel
        backgroundLabel.add(panel);

        // Set the content pane of the frame to the backgroundLabel
        setContentPane(backgroundLabel);

        // Set frame size to match background image
        setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Add ActionListeners to buttons
        manageChitGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageChitGroups().setVisible(true);
            }
        });

        manageCustomersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageCustomers().setVisible(true);
            }
        });

        manageAuctionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAuction newAuction = new NewAuction();
                JFrame auctionFrame = new JFrame("New Auction");
                auctionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                auctionFrame.add(newAuction);
                auctionFrame.pack();
                auctionFrame.setLocationRelativeTo(auctionFrame); // Set relative location to this frame
                auctionFrame.setVisible(true);
            }
        });


        setVisible(true);
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
                new AdminPanel();
            }
        });
    }
}
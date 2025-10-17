import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeScreen::new);
    }
}

class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("XYZ Bank - Welcome");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255)); // Light background

        JLabel welcomeLabel = new JLabel("Welcome to XYZ Bank", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 250, 20, 250));
        buttonPanel.setBackground(new Color(245, 250, 255)); // Match background

        RoundedButton customerLoginBtn = new RoundedButton("Login as Customer");
        RoundedButton customerSignupBtn = new RoundedButton("Customer Signup");
        RoundedButton staffLoginBtn = new RoundedButton("Login as Bank Staff");
        RoundedButton staffSignupBtn = new RoundedButton("Bank Staff Signup");

        buttonPanel.add(customerLoginBtn);
        buttonPanel.add(customerSignupBtn);
        buttonPanel.add(staffLoginBtn);
        buttonPanel.add(staffSignupBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        customerLoginBtn.addActionListener(e -> {
            new View.CustomerLogin();
            dispose();
        });

        customerSignupBtn.addActionListener(e -> {
            new View.CustomerSignUp();
            dispose();
        });

        staffLoginBtn.addActionListener(e -> {
            new View.BankStaffLogin();
            dispose();
        });

        staffSignupBtn.addActionListener(e -> {
            new View.BankStaffSignUp();
            dispose();
        });

        setVisible(true);
    }

    // Custom Rounded Button Class
    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBackground(new Color(173, 216, 230)); // Light pastel blue
            setForeground(Color.BLACK);
            setPreferredSize(new Dimension(180, 35));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border
        }

        @Override
        public boolean isContentAreaFilled() {
            return false;
        }
    }
}

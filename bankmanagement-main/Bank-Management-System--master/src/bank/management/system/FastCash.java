
package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    FastCash(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(
                ClassLoader.getSystemResource("icon/atm2.png")
        );
        Image i2 = i1.getImage().getScaledInstance(
                1550, 830, Image.SCALE_DEFAULT
        );
        ImageIcon i3 = new ImageIcon(i2);

        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 830);
        add(l3);

        JLabel label = new JLabel("SELECT WITHDRAWAL AMOUNT");
        label.setBounds(445, 180, 700, 35);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("System", Font.BOLD, 23));
        l3.add(label);

        b1 = createButton("Rs. 100", 410, 274, l3);
        b2 = createButton("Rs. 500", 700, 274, l3);
        b3 = createButton("Rs. 1000", 410, 318, l3);
        b4 = createButton("Rs. 2000", 700, 318, l3);
        b5 = createButton("Rs. 5000", 410, 362, l3);
        b6 = createButton("Rs. 10000", 700, 362, l3);
        b7 = createButton("BACK", 700, 406, l3);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createButton(
            String text, int x, int y, JLabel panel) {

        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 125, 128));
        button.setBounds(x, y, 150, 35);
        button.addActionListener(this);
        panel.add(button);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b7) {
            dispose();
            new main_Class(pin);
            return;
        }

        JButton clickedButton = (JButton) e.getSource();

        String amountText =
                clickedButton.getText().replace("Rs.", "").trim();

        BigDecimal amount = new BigDecimal(amountText);

        Connn c = new Connn();

        if (c.connection == null) {
            JOptionPane.showMessageDialog(
                    this, "Database connection failed"
            );
            return;
        }

        String lockQuery =
                "SELECT pin FROM login WHERE pin = ? FOR UPDATE";

        String balanceQuery =
                "SELECT type, amount FROM bank WHERE pin = ?";

        String insertQuery =
                "INSERT INTO bank (pin, date, type, amount) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection connection = c.connection) {

            connection.setAutoCommit(false);

            try {
                // Serialize transactions for this demo account.
                try (PreparedStatement ps =
                             connection.prepareStatement(lockQuery)) {

                    ps.setString(1, pin);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Account not found"
                            );
                        }
                    }
                }

                BigDecimal balance = BigDecimal.ZERO;

                try (PreparedStatement ps =
                             connection.prepareStatement(balanceQuery)) {

                    ps.setString(1, pin);

                    try (ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {
                            String type = rs.getString("type");
                            BigDecimal value =
                                    rs.getBigDecimal("amount");

                            if (value == null) {
                                continue;
                            }

                            if ("Deposit".equalsIgnoreCase(type)) {
                                balance = balance.add(value);

                            } else if (
                                    "Withdrawl".equalsIgnoreCase(type)
                                            || "Withdrawal".equalsIgnoreCase(type)
                            ) {
                                balance = balance.subtract(value);
                            }
                        }
                    }
                }

                if (balance.compareTo(amount) < 0) {
                    connection.rollback();

                    JOptionPane.showMessageDialog(
                            this,
                            "Insufficient Balance!\n" +
                                    "Available Balance: Rs. " + balance
                    );
                    return;
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(insertQuery)) {

                    ps.setString(1, pin);
                    ps.setString(2, new Date().toString());
                    ps.setString(3, "Withdrawl");
                    ps.setBigDecimal(4, amount);

                    ps.executeUpdate();
                }

                connection.commit();

                JOptionPane.showMessageDialog(
                        this,
                        "Rs. " + amount +
                                " Debited Successfully!\n" +
                                "Remaining Balance: Rs. " +
                                balance.subtract(amount)
                );

                dispose();
                new main_Class(pin);

            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Fast Cash failed: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        new FastCash("");
    }
}

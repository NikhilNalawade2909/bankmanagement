
package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

public class Withdrawl extends JFrame implements ActionListener {

    String pin;
    TextField textField;
    JButton b1, b2;

    Withdrawl(String pin) {
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

        JLabel label1 = new JLabel(
                "MAXIMUM WITHDRAWAL IS RS.10,000"
        );
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(460, 180, 700, 35);
        l3.add(label1);

        JLabel label2 = new JLabel(
                "PLEASE ENTER YOUR AMOUNT"
        );
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(460, 220, 400, 35);
        l3.add(label2);

        textField = new TextField();
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460, 260, 320, 25);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(textField);

        b1 = new JButton("WITHDRAW");
        b1.setBounds(700, 362, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(700, 406, 150, 35);
        b2.setBackground(new Color(65, 125, 128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b2) {
            dispose();
            new main_Class(pin);
            return;
        }

        if (e.getSource() != b1) {
            return;
        }

        String amountText = textField.getText().trim();

        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter withdrawal amount"
            );
            return;
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Enter a valid numeric amount"
            );
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(
                    this, "Amount must be greater than zero"
            );
            return;
        }

        if (amount.scale() > 2) {
            JOptionPane.showMessageDialog(
                    this, "Maximum 2 decimal places allowed"
            );
            return;
        }

        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            JOptionPane.showMessageDialog(
                    this, "Maximum withdrawal is Rs.10,000"
            );
            return;
        }

        Connn c = new Connn();

        if (c.connection == null) {
            JOptionPane.showMessageDialog(
                    this, "Database connection failed"
            );
            return;
        }

        String balanceQuery =
                "SELECT type, amount FROM bank WHERE pin = ?";

        String insertQuery =
                "INSERT INTO bank (pin, date, type, amount) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection connection = c.connection) {

            // Lock the account row to serialize withdrawals
            // for this account in the demo application.
            connection.setAutoCommit(false);

            try {
                String lockQuery =
                        "SELECT pin FROM login " +
                                "WHERE pin = ? FOR UPDATE";

                try (PreparedStatement lock =
                             connection.prepareStatement(lockQuery)) {
                    lock.setString(1, pin);

                    try (ResultSet rs = lock.executeQuery()) {
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
                                    "Available: Rs. " + balance
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
                                " Debited Successfully\n" +
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
                    "Withdrawal failed: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        new Withdrawl("");
    }
}

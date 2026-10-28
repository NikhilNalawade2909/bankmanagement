
package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Date;

public class Deposit extends JFrame implements ActionListener {

    String pin;
    TextField textField;
    JButton b1, b2;

    Deposit(String pin) {
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
                "ENTER AMOUNT YOU WANT TO DEPOSIT"
        );
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(460, 180, 400, 35);
        l3.add(label1);

        textField = new TextField();
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460, 230, 320, 25);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(textField);

        b1 = new JButton("DEPOSIT");
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
            setVisible(false);
            dispose();
            new main_Class(pin);
            return;
        }

        if (e.getSource() == b1) {

            try {
                String amountText = textField.getText().trim();

                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter the amount you want to deposit"
                    );
                    return;
                }

                BigDecimal amount;

                try {
                    amount = new BigDecimal(amountText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a valid numeric amount"
                    );
                    return;
                }

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Deposit amount must be greater than zero"
                    );
                    return;
                }

                if (amount.scale() > 2 ||
                        amount.compareTo(
                                new BigDecimal("9999999999.99")
                        ) > 0) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Enter an amount up to 9999999999.99 " +
                                    "with a maximum of 2 decimal places"
                    );
                    return;
                }

                Connn c = new Connn();

                if (c.connection == null) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Database connection failed"
                    );
                    return;
                }

                String query =
                        "INSERT INTO bank (pin, date, type, amount) " +
                                "VALUES (?, ?, ?, ?)";

                try (java.sql.Connection connection = c.connection;
                     PreparedStatement ps =
                             connection.prepareStatement(query)) {

                    ps.setString(1, pin);
                    ps.setString(2, new Date().toString());
                    ps.setString(3, "Deposit");
                    ps.setBigDecimal(4, amount);

                    int rows = ps.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Rs. " + amount +
                                        " Deposited Successfully"
                        );

                        setVisible(false);
                        dispose();
                        new main_Class(pin);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Deposit failed. Please try again."
                        );
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Error during deposit: " + ex.getMessage()
                );
            }
        }
    }

    public static void main(String[] args) {
        new Deposit("");
    }
}

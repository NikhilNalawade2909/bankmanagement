
package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BalanceEnquriy extends JFrame implements ActionListener {

    String pin;
    JLabel label2;
    JButton b1;

    BalanceEnquriy(String pin) {
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

        JLabel label1 = new JLabel("Your Current Balance is Rs");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(430, 180, 700, 35);
        l3.add(label1);

        label2 = new JLabel("Loading balance...");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 20));
        label2.setBounds(430, 220, 400, 35);
        l3.add(label2);

        b1 = new JButton("BACK");
        b1.setBounds(700, 406, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        loadBalance();

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadBalance() {

        String query =
                "SELECT COALESCE(SUM(CASE " +
                        "WHEN type = 'Deposit' THEN amount " +
                        "WHEN type IN ('Withdrawl', 'Withdrawal') " +
                        "THEN -amount ELSE 0 END), 0) AS balance, " +
                        "COUNT(*) AS transaction_count " +
                        "FROM bank WHERE pin = ?";

        try {
            Connn c = new Connn();

            if (c.connection == null) {
                label2.setText("Database connection failed");
                return;
            }

            try (Connection connection = c.connection;
                 PreparedStatement ps =
                         connection.prepareStatement(query)) {

                ps.setString(1, pin);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        int count = rs.getInt("transaction_count");

                        if (count == 0) {
                            label2.setText("No transactions found");
                            return;
                        }

                        BigDecimal balance =
                                rs.getBigDecimal("balance");

                        label2.setText(
                                "Rs. " + balance.toPlainString()
                        );
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            label2.setText("Unable to load balance");
            JOptionPane.showMessageDialog(
                    this,
                    "Balance enquiry failed: " + ex.getMessage()
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new main_Class(pin);
    }

    public static void main(String[] args) {
        new BalanceEnquriy("");
    }
}

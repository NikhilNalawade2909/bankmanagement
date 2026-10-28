
package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class mini extends JFrame implements ActionListener {

    String pin;
    JButton button;
    JLabel cardLabel;
    JLabel balanceLabel;
    JTextArea transactionArea;

    mini(String pin) {
        this.pin = pin;

        getContentPane().setBackground(new Color(255, 204, 204));

        setTitle("Mini Statement");
        setSize(500, 650);
        setLocation(100, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("BANK MINI STATEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(110, 20, 350, 30);
        add(title);

        cardLabel = new JLabel("Card Number: Loading...");
        cardLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cardLabel.setBounds(30, 80, 430, 30);
        add(cardLabel);

        JLabel historyLabel = new JLabel("TRANSACTION HISTORY");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        historyLabel.setBounds(30, 130, 400, 30);
        add(historyLabel);

        transactionArea = new JTextArea();
        transactionArea.setEditable(false);
        transactionArea.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );
        transactionArea.setBackground(Color.WHITE);
        transactionArea.setForeground(Color.BLACK);
        transactionArea.setLineWrap(false);

        JScrollPane scrollPane =
                new JScrollPane(transactionArea);

        scrollPane.setBounds(30, 170, 425, 300);
        add(scrollPane);

        balanceLabel = new JLabel("Current Balance: Loading...");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setBounds(30, 490, 440, 30);
        add(balanceLabel);

        button = new JButton("EXIT");
        button.setBounds(175, 545, 130, 35);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        add(button);

        loadMiniStatement();

        setVisible(true);
    }

    private void loadMiniStatement() {

        Connn c = new Connn();

        if (c.connection == null) {
            transactionArea.setText("Database connection failed.");
            balanceLabel.setText("Balance unavailable");
            return;
        }

        String cardQuery =
                "SELECT cardno FROM login WHERE pin = ?";

        String transactionQuery =
                "SELECT date, type, amount " +
                        "FROM bank WHERE pin = ?";

        try (Connection connection = c.connection) {

            // Fetch registered card number
            try (PreparedStatement ps =
                         connection.prepareStatement(cardQuery)) {

                ps.setString(1, pin);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        String cardno = rs.getString("cardno");

                        if (cardno != null && cardno.length() >= 8) {
                            String masked =
                                    cardno.substring(0, 4) +
                                            "XXXXXXXX" +
                                            cardno.substring(cardno.length() - 4);

                            cardLabel.setText(
                                    "Card Number: " + masked
                            );
                        } else {
                            cardLabel.setText(
                                    "Card Number: Unavailable"
                            );
                        }
                    } else {
                        cardLabel.setText(
                                "Card Number: Account not found"
                        );
                    }
                }
            }

            BigDecimal balance = BigDecimal.ZERO;
            int count = 0;

            StringBuilder history = new StringBuilder();

            history.append("DATE / TIME | TYPE | AMOUNT\n");
            history.append("----------------------------------------\n\n");

            try (PreparedStatement ps =
                         connection.prepareStatement(transactionQuery)) {

                ps.setString(1, pin);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        count++;

                        String date = rs.getString("date");
                        String type = rs.getString("type");
                        BigDecimal amount =
                                rs.getBigDecimal("amount");

                        if (amount == null) {
                            continue;
                        }

                        history.append(date)
                                .append("\n")
                                .append(type)
                                .append(" : Rs. ")
                                .append(amount.toPlainString())
                                .append("\n")
                                .append("----------------------------------------\n");

                        if ("Deposit".equalsIgnoreCase(type)) {
                            balance = balance.add(amount);

                        } else if (
                                "Withdrawl".equalsIgnoreCase(type) ||
                                        "Withdrawal".equalsIgnoreCase(type)
                        ) {
                            balance = balance.subtract(amount);
                        }
                    }
                }
            }

            if (count == 0) {
                transactionArea.setText(
                        "No transactions found for this account."
                );
            } else {
                transactionArea.setText(history.toString());
            }

            transactionArea.setCaretPosition(0);

            balanceLabel.setText(
                    "Current Balance: Rs. " +
                            balance.toPlainString()
            );

        } catch (Exception ex) {
            ex.printStackTrace();

            transactionArea.setText(
                    "Unable to load transactions:\n" +
                            ex.getMessage()
            );

            balanceLabel.setText("Balance unavailable");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    public static void main(String[] args) {
        new mini("");
    }
}


package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup3 extends JFrame implements ActionListener {

    JRadioButton r1, r2, r3, r4;
    JCheckBox c1, c2, c3, c4, c5, c6, c7;
    JButton s, c;
    String formno;

    private final SecureRandom random = new SecureRandom();

    Signup3(String formno) {
        super("APPLICATION FORM - PAGE 3");
        this.formno = formno;

        ImageIcon i1 = new ImageIcon(
                ClassLoader.getSystemResource("icon/bank.png")
        );
        Image i2 = i1.getImage().getScaledInstance(
                100, 100, Image.SCALE_SMOOTH
        );
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(150, 5, 100, 100);
        add(image);

        JLabel l1 = new JLabel("Page 3:");
        l1.setFont(new Font("Raleway", Font.BOLD, 22));
        l1.setBounds(280, 40, 400, 40);
        add(l1);

        JLabel l2 = new JLabel("Account Details");
        l2.setFont(new Font("Raleway", Font.BOLD, 22));
        l2.setBounds(280, 70, 400, 40);
        add(l2);

        JLabel l3 = new JLabel("Account Type:");
        l3.setFont(new Font("Raleway", Font.BOLD, 18));
        l3.setBounds(100, 140, 200, 30);
        add(l3);

        r1 = new JRadioButton("Saving Account");
        r1.setBounds(100, 180, 200, 30);

        r2 = new JRadioButton("Fixed Deposit Account");
        r2.setBounds(350, 180, 300, 30);

        r3 = new JRadioButton("Current Account");
        r3.setBounds(100, 220, 250, 30);

        r4 = new JRadioButton("Recurring Deposit Account");
        r4.setBounds(350, 220, 300, 30);

        ButtonGroup accountGroup = new ButtonGroup();
        accountGroup.add(r1);
        accountGroup.add(r2);
        accountGroup.add(r3);
        accountGroup.add(r4);

        JRadioButton[] accountButtons = {r1, r2, r3, r4};

        for (JRadioButton rb : accountButtons) {
            rb.setFont(new Font("Raleway", Font.BOLD, 16));
            rb.setBackground(new Color(215, 252, 252));
            add(rb);
        }

        JLabel l4 = new JLabel("Card Number:");
        l4.setFont(new Font("Raleway", Font.BOLD, 18));
        l4.setBounds(100, 300, 200, 30);
        add(l4);

        JLabel l5 = new JLabel("(Your 16-digit Card Number)");
        l5.setBounds(100, 330, 250, 20);
        add(l5);

        JLabel l6 = new JLabel("XXXX-XXXX-XXXX-4841");
        l6.setFont(new Font("Raleway", Font.BOLD, 18));
        l6.setBounds(330, 300, 300, 30);
        add(l6);

        JLabel l7 = new JLabel(
                "(It will appear on your ATM card and statements)"
        );
        l7.setBounds(330, 330, 450, 20);
        add(l7);

        JLabel l8 = new JLabel("PIN:");
        l8.setFont(new Font("Raleway", Font.BOLD, 18));
        l8.setBounds(100, 370, 200, 30);
        add(l8);

        JLabel l9 = new JLabel("XXXX");
        l9.setFont(new Font("Raleway", Font.BOLD, 18));
        l9.setBounds(330, 370, 200, 30);
        add(l9);

        JLabel l10 = new JLabel("(4-digit PIN)");
        l10.setBounds(100, 400, 200, 20);
        add(l10);

        JLabel l11 = new JLabel("Services Required:");
        l11.setFont(new Font("Raleway", Font.BOLD, 18));
        l11.setBounds(100, 450, 250, 30);
        add(l11);

        c1 = new JCheckBox("ATM CARD");
        c1.setBounds(100, 500, 200, 30);

        c2 = new JCheckBox("Internet Banking");
        c2.setBounds(350, 500, 200, 30);

        c3 = new JCheckBox("Mobile Banking");
        c3.setBounds(100, 550, 200, 30);

        c4 = new JCheckBox("EMAIL Alerts");
        c4.setBounds(350, 550, 200, 30);

        c5 = new JCheckBox("Cheque Book");
        c5.setBounds(100, 600, 200, 30);

        c6 = new JCheckBox("E-Statement");
        c6.setBounds(350, 600, 200, 30);

        JCheckBox[] services = {c1, c2, c3, c4, c5, c6};

        for (JCheckBox cb : services) {
            cb.setFont(new Font("Raleway", Font.BOLD, 16));
            cb.setBackground(new Color(215, 252, 252));
            add(cb);
        }

        c7 = new JCheckBox(
                "I declare that the information provided is correct."
        );
        c7.setFont(new Font("Raleway", Font.BOLD, 12));
        c7.setBounds(100, 680, 600, 20);
        c7.setBackground(new Color(215, 252, 252));
        add(c7);

        JLabel l12 = new JLabel("Form No:");
        l12.setFont(new Font("Raleway", Font.BOLD, 14));
        l12.setBounds(680, 10, 100, 30);
        add(l12);

        JLabel l13 = new JLabel(formno);
        l13.setFont(new Font("Raleway", Font.BOLD, 14));
        l13.setBounds(750, 10, 90, 30);
        add(l13);

        s = new JButton("Submit");
        s.setFont(new Font("Raleway", Font.BOLD, 14));
        s.setBackground(Color.BLACK);
        s.setForeground(Color.WHITE);
        s.setBounds(250, 720, 100, 30);
        s.addActionListener(this);
        add(s);

        c = new JButton("Cancel");
        c.setFont(new Font("Raleway", Font.BOLD, 14));
        c.setBackground(Color.BLACK);
        c.setForeground(Color.WHITE);
        c.setBounds(420, 720, 100, 30);
        c.addActionListener(this);
        add(c);

        getContentPane().setBackground(
                new Color(215, 252, 252)
        );

        setSize(850, 800);
        setLayout(null);
        setLocation(400, 20);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == c) {
            dispose();
            return;
        }

        if (e.getSource() != s) {
            return;
        }

        String atype = null;

        if (r1.isSelected()) {
            atype = "Saving Account";
        } else if (r2.isSelected()) {
            atype = "Fixed Deposit Account";
        } else if (r3.isSelected()) {
            atype = "Current Account";
        } else if (r4.isSelected()) {
            atype = "Recurring Deposit Account";
        }

        if (atype == null) {
            JOptionPane.showMessageDialog(
                    this, "Please select an account type."
            );
            return;
        }

        if (!c7.isSelected()) {
            JOptionPane.showMessageDialog(
                    this, "Please accept the declaration."
            );
            return;
        }

        StringBuilder facilities = new StringBuilder();

        JCheckBox[] services = {c1, c2, c3, c4, c5, c6};

        for (JCheckBox service : services) {
            if (service.isSelected()) {
                if (facilities.length() > 0) {
                    facilities.append(", ");
                }
                facilities.append(service.getText());
            }
        }

        String fac = facilities.toString();

        // Generate a 16-digit demonstration card number
        String cardno = "14099630"
                + String.format("%08d",
                random.nextInt(100000000));

        // Generate a 4-digit PIN
        String pin = String.valueOf(
                1000 + random.nextInt(9000)
        );

        Connn db = new Connn();

        if (db.connection == null) {
            JOptionPane.showMessageDialog(
                    this, "Database connection failed."
            );
            return;
        }

        String q1 = "INSERT INTO signupthree VALUES (?, ?, ?, ?, ?)";
        String q2 = "INSERT INTO login VALUES (?, ?, ?)";

        try {
            db.connection.setAutoCommit(false);

            try (
                    PreparedStatement ps1 =
                            db.connection.prepareStatement(q1);
                    PreparedStatement ps2 =
                            db.connection.prepareStatement(q2)
            ) {
                ps1.setString(1, formno);
                ps1.setString(2, atype);
                ps1.setString(3, cardno);
                ps1.setString(4, pin);
                ps1.setString(5, fac);
                ps1.executeUpdate();

                ps2.setString(1, formno);
                ps2.setString(2, cardno);
                ps2.setString(3, pin);
                ps2.executeUpdate();

                db.connection.commit();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Account Created Successfully!\n"
                            + "Card Number: " + cardno
                            + "\nPIN: " + pin
                            + "\n\nKeep these demonstration credentials."
            );

            new Deposit(pin);
            dispose();

        } catch (Exception ex) {
            try {
                db.connection.rollback();
            } catch (SQLException rollbackError) {
                rollbackError.printStackTrace();
            }

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed: " + ex.getMessage()
            );
        } finally {
            try {
                db.connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Signup3(""));
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;

public class PaymentDialog extends JDialog {
    private final JTextField amountField;
    private final JTextField paymentDateField;
    private final JTextField paymentMethodField;
    private final JButton payButton;
    private final int studentId;

    public PaymentDialog(int studentId) {
        this.studentId = studentId;
        setTitle("Payment");
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(10, 10, 80, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(100, 10, 160, 25);
        add(amountField);

        JLabel paymentDateLabel = new JLabel("Date:");
        paymentDateLabel.setBounds(10, 50, 80, 25);
        add(paymentDateLabel);

        paymentDateField = new JTextField("YYYY-MM-DD");
        paymentDateField.setBounds(100, 50, 160, 25);
        add(paymentDateField);

        JLabel paymentMethodLabel = new JLabel("Method:");
        paymentMethodLabel.setBounds(10, 90, 80, 25);
        add(paymentMethodLabel);

        paymentMethodField = new JTextField();
        paymentMethodField.setBounds(100, 90, 160, 25);
        add(paymentMethodField);

        payButton = new JButton("Pay");
        payButton.setBounds(100, 130, 80, 25);
        add(payButton);

        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        String amount = amountField.getText();
        String paymentDate = paymentDateField.getText();
        String paymentMethod = paymentMethodField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO payments (student_id, amount, payment_date, payment_method) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, studentId);
            stmt.setBigDecimal(2, new BigDecimal(amount));
            stmt.setDate(3, Date.valueOf(paymentDate));
            stmt.setString(4, paymentMethod);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payment recorded successfully");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment");
        }
    }
}


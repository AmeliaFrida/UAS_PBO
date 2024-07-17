/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewResult extends JFrame {
    private final JTable paymentsTable;
    private final DefaultTableModel paymentsTableModel;

    public ViewResult() {
        setTitle("View Payments");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        paymentsTableModel = new DefaultTableModel(new String[]{"Payment ID", "Student Name", "Amount", "Payment Date", "Payment Method"}, 0);
        paymentsTable = new JTable(paymentsTableModel);
        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        scrollPane.setBounds(50, 50, 700, 500);
        add(scrollPane);

        loadPaymentsData();
    }

    private void loadPaymentsData() {
        paymentsTableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT payments.id, students.first_name, students.last_name, payments.amount, payments.payment_date, payments.payment_method " +
                             "FROM payments " +
                             "JOIN students ON payments.student_id = students.id")) {
            while (rs.next()) {
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                paymentsTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        studentName,
                        rs.getBigDecimal("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_method")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewResult().setVisible(true);
            }
        });
    }
}

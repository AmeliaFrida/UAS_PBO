/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas;

import javax.swing.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;

public class GenerateReport extends JFrame {
    private JButton generateReportButton;

    public GenerateReport() {
        setTitle("Generate Report");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(100, 50, 200, 30);
        add(generateReportButton);

        generateReportButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        Document document = new Document();
        String filePath = "StudentPaymentReport.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Student Payment Report"));
            document.add(new Paragraph(" ")); // Blank line

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT students.first_name, students.last_name, payments.amount, payments.payment_date, payments.payment_method " +
                                 "FROM payments " +
                                 "JOIN students ON payments.student_id = students.id")) {
                while (rs.next()) {
                    String studentInfo = "Name: " + rs.getString("first_name") + " " + rs.getString("last_name") +
                            ", Amount: " + rs.getBigDecimal("amount") +
                            ", Date: " + rs.getDate("payment_date") +
                            ", Method: " + rs.getString("payment_method");
                    document.add(new Paragraph(studentInfo));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            document.close();
            JOptionPane.showMessageDialog(this, "Report generated successfully at " + filePath);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GenerateReport().setVisible(true);
            }
        });
    }
}
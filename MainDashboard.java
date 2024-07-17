package com.mycompany.uas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class MainDashboard extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private final JButton registerButton, viewResultsButton, generateReportButton, updateButton, deleteButton;

    public MainDashboard() {
        setTitle("Main Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        registerButton = new JButton("Register Student");
        registerButton.setBounds(50, 50, 200, 30);
        add(registerButton);

        viewResultsButton = new JButton("View Results");
        viewResultsButton.setBounds(50, 100, 200, 30);
        add(viewResultsButton);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(50, 150, 200, 30);
        add(generateReportButton);

        // Table for displaying students
        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "DOB", "Gender", "Address", "Phone", "Email"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(300, 50, 450, 400);
        add(scrollPane);

        // Buttons for update and delete
        updateButton = new JButton("Update Student");
        updateButton.setBounds(300, 470, 200, 30);
        add(updateButton);

        deleteButton = new JButton("Delete Student");
        deleteButton.setBounds(550, 470, 200, 30);
        add(deleteButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationForm().setVisible(true);
            }
        });
        
        viewResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewResult().setVisible(true);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GenerateReport().setVisible(true);
            }
        });
         
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                        new PaymentDialog(studentId).setVisible(true);
                    }
                }
            }
        });

        loadStudentData();
    }

    private void loadStudentData() {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            String lastName = (String) tableModel.getValueAt(selectedRow, 2);
            java.sql.Date dob = (java.sql.Date) tableModel.getValueAt(selectedRow, 3);
            String gender = (String) tableModel.getValueAt(selectedRow, 4);
            String address = (String) tableModel.getValueAt(selectedRow, 5);
            String phone = (String) tableModel.getValueAt(selectedRow, 6);
            String email = (String) tableModel.getValueAt(selectedRow, 7);

            // Display a dialog to update the student information
            JTextField firstNameField = new JTextField(firstName);
            JTextField lastNameField = new JTextField(lastName);
            JTextField dobField = new JTextField(dob.toString());
            JTextField genderField = new JTextField(gender);
            JTextField addressField = new JTextField(address);
            JTextField phoneField = new JTextField(phone);
            JTextField emailField = new JTextField(email);

            Object[] message = {
                    "First Name:", firstNameField,
                    "Last Name:", lastNameField,
                    "Date of Birth:", dobField,
                    "Gender:", genderField,
                    "Address:", addressField,
                    "Phone:", phoneField,
                    "Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Update Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
                     PreparedStatement stmt = conn.prepareStatement("UPDATE students SET first_name = ?, last_name = ?, dob = ?, gender = ?, address = ?, phone = ?, email = ? WHERE id = ?")) {
                    stmt.setString(1, firstNameField.getText());
                    stmt.setString(2, lastNameField.getText());
                    stmt.setDate(3, java.sql.Date.valueOf(dobField.getText()));
                    stmt.setString(4, genderField.getText());
                    stmt.setString(5, addressField.getText());
                    stmt.setString(6, phoneField.getText());
                    stmt.setString(7, emailField.getText());
                    stmt.setInt(8, id);
                    stmt.executeUpdate();
                    loadStudentData();
                    JOptionPane.showMessageDialog(this, "Student updated successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating student");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update");
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Delete Student", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
                     PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                    loadStudentData();
                    JOptionPane.showMessageDialog(this, "Student deleted successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting student");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainDashboard().setVisible(true);
            }
        });
    }
}

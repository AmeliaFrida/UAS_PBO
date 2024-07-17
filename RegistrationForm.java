/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uas;
import javax.swing.*;
import org.jdatepicker.impl.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.jdatepicker.DateLabelFormatter;

public class RegistrationForm extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JDatePickerImpl datePicker;
    private JComboBox<String> genderComboBox;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton registerButton;

    public RegistrationForm() {
        setTitle("Student Registration");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerStudent();
            }
        });
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(10, 20, 80, 25);
        panel.add(firstNameLabel);

        firstNameField = new JTextField(20);
        firstNameField.setBounds(150, 20, 200, 25);
        panel.add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(10, 50, 80, 25);
        panel.add(lastNameLabel);

        lastNameField = new JTextField(20);
        lastNameField.setBounds(150, 50, 200, 25);
        panel.add(lastNameField);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(10, 80, 80, 25);
        panel.add(dobLabel);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(150, 80, 200, 30);
        panel.add(datePicker);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(10, 110, 80, 25);
        panel.add(genderLabel);

        genderComboBox = new JComboBox<>(new String[]{"male", "female"});
        genderComboBox.setBounds(150, 110, 200, 25);
        panel.add(genderComboBox);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(10, 140, 80, 25);
        panel.add(addressLabel);

        addressField = new JTextField(20);
        addressField.setBounds(150, 140, 200, 25);
        panel.add(addressField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(10, 170, 80, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField(20);
        phoneField.setBounds(150, 170, 200, 25);
        panel.add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 200, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(150, 200, 200, 25);
        panel.add(emailField);

        registerButton = new JButton("Register");
        registerButton.setBounds(150, 230, 200, 25);
        panel.add(registerButton);
    }

    private void registerStudent() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
        java.sql.Date dob = new java.sql.Date(selectedDate.getTime());
        String gender = (String) genderComboBox.getSelectedItem();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pendaftaran", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (first_name, last_name, dob, gender, address, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setDate(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, address);
            stmt.setString(6, phone);
            stmt.setString(7, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student registered successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering student");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistrationForm().setVisible(true);
            }
        });
    }
}

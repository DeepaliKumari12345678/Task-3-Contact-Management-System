package com.deepali;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class ContactManager extends JFrame {
    private JTextField nameField, phoneField, emailField;
    private JButton addButton, viewButton;
    private JTextArea displayArea;

    public ContactManager() {
        setTitle("Contact Management System");
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue
        JLabel headerLabel = new JLabel("Contact Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(240, 248, 255)); // Alice blue

        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        emailField = new JTextField(15);
        addButton = new JButton("Add Contact");
        viewButton = new JButton("View Contacts");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(addButton);
        formPanel.add(viewButton);

        // Display panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayPanel.setBackground(new Color(240, 248, 255)); // Alice blue
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewContacts();
            }
        });

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contact added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewContacts() {
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contacts")) {
            displayArea.setText("");
            while (rs.next()) {
                displayArea.append("ID: " + rs.getInt("id") + "\n");
                displayArea.append("Name: " + rs.getString("name") + "\n");
                displayArea.append("Phone: " + rs.getString("phone") + "\n");
                displayArea.append("Email: " + rs.getString("email") + "\n\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ContactManager();
    }
}


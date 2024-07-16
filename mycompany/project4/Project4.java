/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.project4;

/**
 *
 * @author fasta
 */
import javax.swing.SwingUtilities;
public class Project4 {
    public static void main(String[] args) {
        // Menggunakan SwingUtilities untuk menjalankan GUI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Membuka Login Screen saat aplikasi dimulai
            new LoginScreen().setVisible(true);
        });
}

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

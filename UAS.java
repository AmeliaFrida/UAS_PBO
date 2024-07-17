/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.uas;

/**
 *
 * @author Rifqi
 */

import javax.swing.SwingUtilities;
public class UAS {
    public static void main(String[] args) {
        // Menggunakan SwingUtilities untuk menjalankan GUI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Membuka Login Screen saat aplikasi dimulai
            new LoginScreen().setVisible(true);
        });
}
}
    

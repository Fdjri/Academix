package view;

import model.DatabaseConnection;
import model.Dosen;
import view.MataKuliah;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;


public class FrameMahasiswa extends javax.swing.JFrame {

    public FrameMahasiswa(String nama, String nim) {
        initComponents();
        TNama.setText(nama);
        TNama.setEditable(false);
        TNim.setText(nim);
        TNim.setEditable(false);
        
        loadSelectedCourses(nim);

        BTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MataKuliah().setVisible(true);
            }
        });
        
        BUbah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                updateCourse();
            }
        });
        
        BHapus.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                deleteCourse();
            }
        });
    }
    
    // Metode untuk memuat data nama dosen dari database berdasarkan ID dosen
    private Map<Integer, String> loadNamaDosen() {
    Map<Integer, String> namaDosenMap = new HashMap<>();
    
    // Buat koneksi ke database
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.koneksiDatabase();

    // Query untuk mengambil data dari tabel dosen
    String query = "SELECT * FROM dosen";

    try {
        // Persiapkan statement SQL
        PreparedStatement statement = connection.prepareStatement(query);

        // Eksekusi query
        ResultSet resultSet = statement.executeQuery();

        // Iterasi hasil query dan tambahkan ke dalam map
        while (resultSet.next()) {
            int idDosen = resultSet.getInt("id_dosen");
            String namaDosen = resultSet.getString("nama_dosen");
            namaDosenMap.put(idDosen, namaDosen);
        }

        // Tutup statement dan koneksi
        statement.close();
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return namaDosenMap;
}
    
    // Metode untuk memuat data dari database ke dalam tabel
    private void loadSelectedCourses(String nim) {
    // Buat objek DefaultTableModel untuk menyimpan data dari database
    DefaultTableModel model = (DefaultTableModel) TabelKrs.getModel();
    // Kosongkan tabel
    model.setRowCount(0);
    
    // Buat koneksi ke database
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.koneksiDatabase();
    
    // Mendapatkan map dari ID dosen ke nama dosen
    Map<Integer, String> namaDosenMap = loadNamaDosen();

    // Query untuk mengambil data dari tabel krs berdasarkan NIM mahasiswa
    String query = "SELECT * FROM krs WHERE nim = ?";
    
    try {
        // Persiapkan statement SQL
        PreparedStatement statement = connection.prepareStatement(query);
        
        // Set parameter query
        statement.setString(1, nim);

        // Eksekusi query
        ResultSet resultSet = statement.executeQuery();

        // Iterasi hasil query dan tambahkan ke dalam model tabel
        while (resultSet.next()) {
            int kd_mk = resultSet.getInt("kd_mk");
            
            // Ambil data mata kuliah berdasarkan kd_mk
            String queryMatkul = "SELECT * FROM mata_kuliah WHERE kd_mk = ?";
            PreparedStatement statementMatkul = connection.prepareStatement(queryMatkul);
            statementMatkul.setInt(1, kd_mk);
            ResultSet rsMatkul = statementMatkul.executeQuery();
            if (rsMatkul.next()) {
                int idDosen = rsMatkul.getInt("dosen");
                String namaDosen = namaDosenMap.get(idDosen); // Mendapatkan nama dosen berdasarkan ID dosen
                
                // Tambahkan data ke dalam model tabel
                Object[] row = {
                    rsMatkul.getInt("kd_mk"),
                    rsMatkul.getString("nama_mk"),
                    rsMatkul.getInt("sks"),
                    namaDosen // Menggunakan nama dosen
                };
                model.addRow(row);
            }
            statementMatkul.close();
        }

        // Tutup statement dan koneksi
        statement.close();
        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void addCourse(String nim, int kd_mk) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.koneksiDatabase();

        String query = "INSERT INTO krs (nim, kd_mk) VALUES (?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nim);
            statement.setInt(2, kd_mk);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateCourse() {
        int selectedRow = TabelKrs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan diubah", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kd_mk = (int) TabelKrs.getValueAt(selectedRow, 0);
        String namaMk = (String) TabelKrs.getValueAt(selectedRow, 1);
        int sks = (int) TabelKrs.getValueAt(selectedRow, 2);
        String dosen = (String) TabelKrs.getValueAt(selectedRow, 3);

        JTextField namaMkField = new JTextField(namaMk);
        JTextField sksField = new JTextField(String.valueOf(sks));
        JTextField dosenField = new JTextField(dosen);

        Object[] message = {
            "Nama Mata Kuliah:", namaMkField,
            "SKS:", sksField,
            "Dosen:", dosenField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ubah Mata Kuliah", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            namaMk = namaMkField.getText();
            sks = Integer.parseInt(sksField.getText());
            dosen = dosenField.getText();

            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.koneksiDatabase();

            String query = "UPDATE mata_kuliah SET nama_mk = ?, sks = ?, dosen = ? WHERE kd_mk = ?";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, namaMk);
                statement.setInt(2, sks);
                statement.setString(3, dosen);
                statement.setInt(4, kd_mk);
                statement.executeUpdate();

                statement.close();
                connection.close();

                loadSelectedCourses(TNim.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteCourse() {
        int selectedRow = TabelKrs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kd_mk = (int) TabelKrs.getValueAt(selectedRow, 0);

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.koneksiDatabase();

        String query = "DELETE FROM krs WHERE kd_mk = ? AND nim = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, kd_mk);
            statement.setString(2, TNim.getText());
            statement.executeUpdate();

            statement.close();
            connection.close();

            loadSelectedCourses(TNim.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        TNama = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        TNim = new javax.swing.JTextField();
        BCari = new javax.swing.JButton();
        TCari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelKrs = new javax.swing.JTable();
        BTambah = new javax.swing.JButton();
        BUbah = new javax.swing.JButton();
        BHapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AcademiX");

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NAMA");

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("NIM");

        BCari.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        BCari.setText("CARI");
        BCari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BCariMouseReleased(evt);
            }
        });
        BCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BCariActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setOpaque(false);

        TabelKrs.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        TabelKrs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "SKS", "Dosen"
            }
        ));
        TabelKrs.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(TabelKrs);

        BTambah.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        BTambah.setText("TAMBAH");
        BTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BTambahMouseReleased(evt);
            }
        });
        BTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTambahActionPerformed(evt);
            }
        });

        BUbah.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        BUbah.setText("UBAH");
        BUbah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BUbahMouseReleased(evt);
            }
        });

        BHapus.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        BHapus.setText("HAPUS");
        BHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BHapusMouseReleased(evt);
            }
        });

        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TNama, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TNim, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(BCari, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TCari, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(BTambah, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(BUbah, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(BHapus, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(BTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BUbah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BHapus))
                    .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDesktopPane1Layout.createSequentialGroup()
                            .addGap(42, 42, 42)
                            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(BCari, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(40, 40, 40)
                            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TNama, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TNim, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TCari, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jDesktopPane1Layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(47, 47, 47)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BCari, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTambah)
                    .addComponent(BUbah)
                    .addComponent(BHapus))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BCariMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCariMouseReleased
    String keyword = TCari.getText().trim();

        DefaultTableModel model = (DefaultTableModel) TabelKrs.getModel();
        model.setRowCount(0);

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.koneksiDatabase();

        Map<Integer, String> namaDosenMap = loadNamaDosen();

        String query = "SELECT * FROM mata_kuliah WHERE nama_mk LIKE ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idDosen = resultSet.getInt("dosen");
                String namaDosen = namaDosenMap.get(idDosen);
                Object[] row = {
                    resultSet.getInt("kd_mk"),
                    resultSet.getString("nama_mk"),
                    resultSet.getInt("sks"),
                    namaDosen
                };
                model.addRow(row);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_BCariMouseReleased

    private void BTambahMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BTambahMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BTambahMouseReleased

    private void BUbahMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BUbahMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BUbahMouseReleased

    private void BHapusMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BHapusMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BHapusMouseReleased

    private void BCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BCariActionPerformed

    private void BTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTambahActionPerformed
    // Menutup frame MataKuliah sebelumnya (jika ada)
    for (java.awt.Window window : java.awt.Window.getWindows()) {
        if (window instanceof MataKuliah) {
            window.dispose();
        }
    }
    // Membuat instance baru dari MataKuliah.java
    MataKuliah mataKuliahFrame = new MataKuliah();
    // Menampilkan frame MataKuliah.java
    mataKuliahFrame.setVisible(true);
    // Mengatur frame MataKuliah.java agar tidak dijalankan lebih dari satu kali
    mataKuliahFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_BTambahActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MataKuliah().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BCari;
    private javax.swing.JButton BHapus;
    private javax.swing.JButton BTambah;
    private javax.swing.JButton BUbah;
    private javax.swing.JTextField TCari;
    private javax.swing.JTextField TNama;
    private javax.swing.JTextField TNim;
    private javax.swing.JTable TabelKrs;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    void refreshTable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

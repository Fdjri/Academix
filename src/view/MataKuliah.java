package view;

import model.DatabaseConnection;
import view.FrameMahasiswa;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class MataKuliah extends javax.swing.JFrame {
    private Map<String, String[]> mataKuliahMap;
    private Map<String, String[]> dosenMap;
    private FrameMahasiswa frameMahasiswa;

    public MataKuliah(FrameMahasiswa frameMahasiswa) {
        initComponents();
        this.frameMahasiswa = frameMahasiswa;
        mataKuliahMap = loadMataKuliah();
        dosenMap = loadDosen();
        for (String namaMataKuliah : mataKuliahMap.keySet()) {
            CMatkul.addItem(namaMataKuliah);
        }
        CMatkul.addActionListener(evt -> CMatkulActionPerformed(evt));
    }

    public MataKuliah() {
        initComponents();
        mataKuliahMap = loadMataKuliah();
        dosenMap = loadDosen();
        for (String namaMataKuliah : mataKuliahMap.keySet()) {
            CMatkul.addItem(namaMataKuliah);
        }
        CMatkul.addActionListener(evt -> CMatkulActionPerformed(evt));
    }

    private Map<String, String[]> loadDosen() {
        Map<String, String[]> dosenMap = new HashMap<>();
        try (Connection connection = DatabaseConnection.koneksiDatabase()) {
            String query = "SELECT id_dosen, nama_dosen FROM dosen";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String idDosen = resultSet.getString("id_dosen");
                    String namaDosen = resultSet.getString("nama_dosen");
                    dosenMap.put(namaDosen, new String[]{idDosen});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dosenMap;
    }

    private Map<String, String[]> loadMataKuliah() {
        Map<String, String[]> mataKuliahMap = new HashMap<>();
        try (Connection connection = DatabaseConnection.koneksiDatabase()) {
            String query = "SELECT nama_mk, kd_mk, sks, dosen, kelas FROM mata_kuliah";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String namaMataKuliah = resultSet.getString("nama_mk");
                    String kdMataKuliah = resultSet.getString("kd_mk");
                    String sks = resultSet.getString("sks");
                    String idDosen = resultSet.getString("dosen");
                    String kelas = resultSet.getString("kelas");
                    String[] data = {kdMataKuliah, sks, idDosen, kelas};
                    mataKuliahMap.put(namaMataKuliah, data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mataKuliahMap;
    }
    
    private void tambahMataKuliah(String namaMatkul, String kodeMatkul, String sks, String idDosen, String kelasMatkul) {
    try (Connection connection = DatabaseConnection.koneksiDatabase()) {
        // Cek apakah ID sudah ada di database
        String checkQuery = "SELECT COUNT(*) FROM mata_kuliah WHERE kd_mk = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, kodeMatkul);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "ID sudah ada dalam database. silakan masukkan id unik");
                    return; // Keluar dari metode jika ID sudah ada
                }
            }
        }

        // Jika ID belum ada, lakukan insert
        String query = "INSERT INTO mata_kuliah (nama_mk, kd_mk, sks, dosen, kelas) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, namaMatkul);
            statement.setString(2, kodeMatkul);
            statement.setString(3, sks);
            statement.setString(4, idDosen);
            statement.setString(5, kelasMatkul);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan");

                // Memperbarui tabel di FrameMahasiswa.java
                if (frameMahasiswa != null) {
                    frameMahasiswa.refreshTable();
                }

                // Menutup frame MataKuliah.java
                dispose();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data");
    }
}


private void CMatkulActionPerformed(java.awt.event.ActionEvent evt) {
    String selectedMatkul = (String) CMatkul.getSelectedItem();
    if (selectedMatkul != null) {
        String[] matkulData = mataKuliahMap.get(selectedMatkul);
        if (matkulData != null && matkulData.length >= 4) {
            TKodeMatkul.setText(matkulData[0]);
            TSks.setText(matkulData[1]);
            
            // Mendapatkan nama dosen dan kelas untuk mata kuliah yang dipilih
            String idDosen = matkulData[2];
            String kelasMatkul = matkulData[3];
            
            // Mendapatkan nama dosen berdasarkan ID dari tabel dosen
            String namaDosen = getNamaDosenById(idDosen);
            
            // Menampilkan nama dosen dan kelas di TDosen dan TKelas
            TDosen.setText(namaDosen);
            TKelas.setText(kelasMatkul);
        }
    }
}

// Metode untuk mendapatkan nama dosen berdasarkan ID dari tabel dosen
private String getNamaDosenById(String idDosen) {
    String namaDosen = "";
    try (Connection connection = DatabaseConnection.koneksiDatabase()) {
        String query = "SELECT nama_dosen FROM dosen WHERE id_dosen = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idDosen);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                namaDosen = resultSet.getString("nama_dosen");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return namaDosen;
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
        CMatkul = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        TKodeMatkul = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TSks = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        BSimpan = new javax.swing.JButton();
        TDosen = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TKelas = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AcademiX");

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nama Mata Kuliah");

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kode Mata Kuliah");

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SKS");

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Dosen");

        BSimpan.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        BSimpan.setText("SIMPAN");
        BSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                BSimpanMouseReleased(evt);
            }
        });
        BSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSimpanActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kelas");

        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(CMatkul, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TKodeMatkul, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TSks, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(BSimpan, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TDosen, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(TKelas, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(35, 35, 35)
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CMatkul, 0, 190, Short.MAX_VALUE)
                            .addComponent(TKodeMatkul)
                            .addComponent(TSks)
                            .addComponent(TDosen)
                            .addComponent(TKelas)))
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(BSimpan)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(CMatkul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TKodeMatkul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TSks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(13, 13, 13)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TDosen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(BSimpan)
                .addGap(51, 51, 51))
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

    private void BSimpanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSimpanMouseReleased
    // Mendapatkan data yang diinput oleh pengguna
    String namaMatkul = (String) CMatkul.getSelectedItem();
    String kodeMatkul = TKodeMatkul.getText();
    String sks = TSks.getText();
    String namaDosen = TDosen.getText();
    String kelasMatkul = TKelas.getText();

    // Periksa apakah namaMatkul dan namaDosen tidak null sebelum melanjutkan
    if (namaMatkul != null && namaDosen != null) {
        // Mendapatkan ID dosen dari peta dosenMap
        String[] dosenData = dosenMap.get(namaDosen);
        if (dosenData != null && dosenData.length > 0) {
            String idDosen = dosenData[0];

            // Lakukan pengecekan dan penyimpanan data ke database
            tambahMataKuliah(namaMatkul, kodeMatkul, sks, idDosen, kelasMatkul);
        } else {
            JOptionPane.showMessageDialog(this, "ID dosen tidak ditemukan");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Mohon pilih mata kuliah dan dosen terlebih dahulu");
    }
    }//GEN-LAST:event_BSimpanMouseReleased

    private void BSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSimpanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BSimpanActionPerformed

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
            java.util.logging.Logger.getLogger(MataKuliah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MataKuliah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MataKuliah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MataKuliah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton BSimpan;
    private javax.swing.JComboBox<String> CMatkul;
    private javax.swing.JTextField TDosen;
    private javax.swing.JTextField TKelas;
    private javax.swing.JTextField TKodeMatkul;
    private javax.swing.JTextField TSks;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}

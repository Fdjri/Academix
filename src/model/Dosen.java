package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dosen {
    // Metode untuk mendapatkan semua data dosen dari database
    public static void getDataDosen() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Membuat koneksi ke database
            connection = DatabaseConnection.koneksiDatabase();

            // Query untuk mendapatkan data dosen
            String query = "SELECT * FROM dosen";
            statement = connection.prepareStatement(query);

            // Menjalankan query
            resultSet = statement.executeQuery();

            // Menampilkan data dosen
            while (resultSet.next()) {
                System.out.println("ID Dosen: " + resultSet.getInt("id_dosen"));
                System.out.println("Nama Dosen: " + resultSet.getString("nama_dosen"));
                System.out.println("Kelas: " + resultSet.getString("kelas"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

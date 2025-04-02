package model;

import java.sql.*;

public class DatabaseConnection {
    private static Connection koneksi = null;

    // Metode untuk membuat koneksi ke database
    public static Connection koneksiDatabase(){
        try{
            // setting driver mysql
            Class.forName("com.mysql.jdbc.Driver");
            // buat connection
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost/academix", "root", "");
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("Connection Error : " + e.getMessage());
        }   
        return koneksi;
    }

    // Metode untuk melakukan login
    public static boolean login(String nama, String nim) {
        boolean isSuccess = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Membuat koneksi ke database
            koneksi = koneksiDatabase();
            
            // Query untuk melakukan pengecekan login
            String query = "SELECT * FROM mahasiswa WHERE nama = ? AND nim = ?";
            statement = koneksi.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, nim);
            
            // Menjalankan query
            resultSet = statement.executeQuery();
            
            // Jika query menghasilkan baris, berarti login berhasil
            if (resultSet.next()) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (koneksi != null) {
                    koneksi.close();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        return isSuccess;
    }
}

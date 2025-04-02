package model;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    private String nama;
    private String nim;

    public Mahasiswa(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    // Getter dan setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    // Fungsi untuk mendapatkan data mahasiswa dari database
    public static Mahasiswa getMahasiswaByNim(String nim) {
        Mahasiswa mahasiswa = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = new DatabaseConnection().koneksiDatabase();
            String query = "SELECT * FROM mahasiswa WHERE nim = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, nim);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nama = resultSet.getString("nama");
                mahasiswa = new Mahasiswa(nama, nim);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        return mahasiswa;
    }

    // Fungsi untuk mendapatkan daftar mahasiswa dari database
    public static List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = new DatabaseConnection().koneksiDatabase();
            String query = "SELECT * FROM mahasiswa";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nama = resultSet.getString("nama");
                String nim = resultSet.getString("nim");
                Mahasiswa mahasiswa = new Mahasiswa(nama, nim);
                mahasiswaList.add(mahasiswa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        return mahasiswaList;
    }
}

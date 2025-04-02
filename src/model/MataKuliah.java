package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MataKuliah {
    private int kd_mk;
    private String nama_mk;
    private int sks;
    private int id_dosen;

    public MataKuliah(int kd_mk, String nama_mk, int sks, int id_dosen) {
        this.kd_mk = kd_mk;
        this.nama_mk = nama_mk;
        this.sks = sks;
        this.id_dosen = id_dosen;
    }

    // Getter dan setter
    public int getKd_mk() {
        return kd_mk;
    }

    public void setKd_mk(int kd_mk) {
        this.kd_mk = kd_mk;
    }

    public String getNama_mk() {
        return nama_mk;
    }

    public void setNama_mk(String nama_mk) {
        this.nama_mk = nama_mk;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public int getId_dosen() {
        return id_dosen;
    }

    public void setId_dosen(int id_dosen) {
        this.id_dosen = id_dosen;
    }

    // Fungsi untuk mendapatkan semua mata kuliah dari database
    public static List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> mataKuliahList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = new DatabaseConnection().koneksiDatabase();
            String query = "SELECT * FROM mata_kuliah";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int kd_mk = resultSet.getInt("kd_mk");
                String nama_mk = resultSet.getString("nama_mk");
                int sks = resultSet.getInt("sks");
                int id_dosen = resultSet.getInt("dosen");
                MataKuliah mataKuliah = new MataKuliah(kd_mk, nama_mk, sks, id_dosen);
                mataKuliahList.add(mataKuliah);
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

        return mataKuliahList;
    }
}

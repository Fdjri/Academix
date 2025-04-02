package model;

import java.util.ArrayList;
import java.util.List;

public class KRS {
    private String nim;
    private List<MataKuliah> mataKuliahList;

    public KRS(String nim) {
        this.nim = nim;
        this.mataKuliahList = new ArrayList<>();
    }

    // Getter dan setter
    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public List<MataKuliah> getMataKuliahList() {
        return mataKuliahList;
    }

    public void setMataKuliahList(List<MataKuliah> mataKuliahList) {
        this.mataKuliahList = mataKuliahList;
    }

    // Metode untuk menambah mata kuliah ke dalam KRS
    public void tambahMataKuliah(MataKuliah mataKuliah) {
        mataKuliahList.add(mataKuliah);
    }

    // Metode untuk menghapus mata kuliah dari KRS
    public void hapusMataKuliah(MataKuliah mataKuliah) {
        mataKuliahList.remove(mataKuliah);
    }
}

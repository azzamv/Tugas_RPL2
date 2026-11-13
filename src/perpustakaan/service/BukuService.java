package perpustakaan.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perpustakaan.dao.BukuDao;
import perpustakaan.model.Buku;

@Service
@Transactional
public class BukuService {
    @Autowired
    private BukuDao dao;

    public List<Buku> semua() { return dao.semua(); }
    public List<Buku> stokTipis() { return dao.stokTipis(3); }
    public long hitung() { return dao.hitung(); }

    public void simpan(String judul, String pengarang, String penerbit, String tahun, String stok) {
        Buku b = new Buku();
        isi(b, judul, pengarang, penerbit, tahun, stok);
        dao.simpan(b);
    }

    public void ubah(int id, String judul, String pengarang, String penerbit, String tahun, String stok) {
        Buku b = dao.cari(id);
        if (b == null) throw new RuntimeException("Buku tidak ditemukan");
        isi(b, judul, pengarang, penerbit, tahun, stok);
        dao.ubah(b);
    }

    public void hapus(int id) { dao.hapus(id); }

    private void isi(Buku b, String judul, String pengarang, String penerbit, String tahun, String stok) {
        if (judul == null || judul.trim().isEmpty()) throw new RuntimeException("Judul wajib diisi");
        b.setJudul(judul.trim());
        b.setPengarang(pengarang == null ? "" : pengarang.trim());
        b.setPenerbit(penerbit == null ? "" : penerbit.trim());
        b.setTahun(tahun != null && !tahun.trim().isEmpty() ? Integer.parseInt(tahun.trim()) : null);
        int s = (stok == null || stok.trim().isEmpty()) ? 0 : Integer.parseInt(stok.trim());
        if (s < 0) throw new RuntimeException("Stok tidak boleh negatif");
        b.setStok(s);
    }
}

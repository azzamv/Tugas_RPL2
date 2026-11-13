package perpustakaan.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perpustakaan.dao.AnggotaDao;
import perpustakaan.model.Anggota;

@Service
@Transactional
public class AnggotaService {
    @Autowired
    private AnggotaDao dao;

    public List<Anggota> semua() { return dao.semua(); }
    public List<Anggota> aktif() { return dao.aktif(); }
    public long hitungAktif() { return dao.hitungAktif(); }

    public void simpan(String nama, String alamat, String telp, String status) {
        if (nama == null || nama.trim().isEmpty()) throw new RuntimeException("Nama wajib diisi");
        Anggota a = new Anggota();
        a.setNama(nama.trim());
        a.setAlamat(alamat == null ? "" : alamat.trim());
        a.setTelepon(telp == null ? "" : telp.trim());
        a.setStatus(status == null ? "Aktif" : status);
        dao.simpan(a);
    }

    public void ubah(int id, String nama, String alamat, String telp, String status) {
        Anggota a = dao.cari(id);
        if (a == null) throw new RuntimeException("Anggota tidak ditemukan");
        if (nama == null || nama.trim().isEmpty()) throw new RuntimeException("Nama wajib diisi");
        a.setNama(nama.trim());
        a.setAlamat(alamat == null ? "" : alamat.trim());
        a.setTelepon(telp == null ? "" : telp.trim());
        a.setStatus(status == null ? "Aktif" : status);
        dao.ubah(a);
    }

    public void hapus(int id) { dao.hapus(id); }
}

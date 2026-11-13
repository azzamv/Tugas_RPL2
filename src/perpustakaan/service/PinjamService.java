package perpustakaan.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perpustakaan.dao.AnggotaDao;
import perpustakaan.dao.BukuDao;
import perpustakaan.dao.PinjamDao;
import perpustakaan.model.Anggota;
import perpustakaan.model.Buku;
import perpustakaan.model.Pinjam;

@Service
@Transactional
public class PinjamService {
    @Autowired
    private PinjamDao dao;
    @Autowired
    private AnggotaDao anggotaDao;
    @Autowired
    private BukuDao bukuDao;

    public List<Pinjam> semua() { return dao.semua(); }
    public List<Pinjam> terbaru() { return dao.terbaru(10); }
    public long hariIni() { return dao.hitungHariIni(); }

    public List<Anggota> anggotaAktif() { return anggotaDao.aktif(); }
    public List<Buku> bukuAda() {
        return bukuDao.semua().stream().filter(b -> b.getStok() != null && b.getStok() > 0).toList();
    }

    public void pinjam(Integer idAnggota, Integer idBuku, Integer idPetugas) {
        if (idAnggota == null || idBuku == null || idPetugas == null)
            throw new RuntimeException("Pilih anggota dan buku");
        dao.pinjam(idAnggota, idBuku, idPetugas);
    }

    public void kembali(int idPinjam) { dao.kembali(idPinjam); }
}

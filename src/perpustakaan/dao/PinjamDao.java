package perpustakaan.dao;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import perpustakaan.model.Anggota;
import perpustakaan.model.Buku;
import perpustakaan.model.Pinjam;
import perpustakaan.model.Petugas;

@Repository
public class PinjamDao {
    @Autowired
    private SessionFactory sf;

    private Session s() { return sf.getCurrentSession(); }

    public List<Pinjam> semua() {
        return s().createQuery(
                "select p from Pinjam p join fetch p.anggota join fetch p.buku join fetch p.petugas order by p.id desc",
                Pinjam.class).list();
    }

    public List<Pinjam> terbaru(int n) {
        return s().createQuery(
                "select p from Pinjam p join fetch p.anggota join fetch p.buku order by p.id desc",
                Pinjam.class).setMaxResults(n).list();
    }

    public Pinjam cari(int id) { return s().get(Pinjam.class, id); }
    public void simpan(Pinjam p) { s().persist(p); }

    public void pinjam(int idAnggota, int idBuku, int idPetugas) {
        Anggota a = s().get(Anggota.class, idAnggota);
        Buku b = s().get(Buku.class, idBuku);
        Petugas pt = s().get(Petugas.class, idPetugas);
        if (a == null || b == null || pt == null) throw new RuntimeException("Data tidak lengkap");
        if (b.getStok() == null || b.getStok() <= 0) throw new RuntimeException("Stok habis");
        Pinjam p = new Pinjam();
        p.setAnggota(a); p.setBuku(b); p.setPetugas(pt);
        p.setTglPinjam(LocalDate.now());
        p.setStatus("Dipinjam");
        b.setStok(b.getStok() - 1);
        s().persist(p);
        s().merge(b);
    }

    public void kembali(int idPinjam) {
        Pinjam p = s().get(Pinjam.class, idPinjam);
        if (p == null) throw new RuntimeException("Transaksi tidak ada");
        if ("Kembali".equalsIgnoreCase(p.getStatus())) throw new RuntimeException("Sudah dikembalikan");
        p.setStatus("Kembali");
        p.setTglKembali(LocalDate.now());
        Buku b = p.getBuku();
        b.setStok((b.getStok() == null ? 0 : b.getStok()) + 1);
        s().merge(p);
        s().merge(b);
    }

    public long hitungHariIni() {
        Long c = s().createQuery("select count(p) from Pinjam p where p.tglPinjam=:d", Long.class)
                .setParameter("d", LocalDate.now()).uniqueResult();
        return c == null ? 0 : c;
    }
}

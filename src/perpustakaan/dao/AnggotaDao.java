package perpustakaan.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import perpustakaan.model.Anggota;

@Repository
public class AnggotaDao {
    @Autowired
    private SessionFactory sf;

    private Session s() { return sf.getCurrentSession(); }

    public List<Anggota> semua() {
        return s().createQuery("from Anggota a order by a.id desc", Anggota.class).list();
    }

    public List<Anggota> aktif() {
        return s().createQuery("from Anggota a where a.status='Aktif' order by a.nama", Anggota.class).list();
    }

    public Anggota cari(int id) { return s().get(Anggota.class, id); }
    public void simpan(Anggota a) { s().persist(a); }
    public void ubah(Anggota a) { s().merge(a); }
    public void hapus(int id) {
        Anggota a = cari(id);
        if (a != null) s().remove(a);
    }

    public long hitungAktif() {
        Long c = s().createQuery("select count(a) from Anggota a where a.status='Aktif'", Long.class).uniqueResult();
        return c == null ? 0 : c;
    }
}

package perpustakaan.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import perpustakaan.model.Buku;

@Repository
public class BukuDao {
    @Autowired
    private SessionFactory sf;

    private Session s() { return sf.getCurrentSession(); }

    public List<Buku> semua() {
        return s().createQuery("from Buku b order by b.id desc", Buku.class).list();
    }

    public Buku cari(int id) { return s().get(Buku.class, id); }
    public void simpan(Buku b) { s().persist(b); }
    public void ubah(Buku b) { s().merge(b); }
    public void hapus(int id) {
        Buku b = cari(id);
        if (b != null) s().remove(b);
    }

    public long hitung() {
        Long c = s().createQuery("select count(b) from Buku b", Long.class).uniqueResult();
        return c == null ? 0 : c;
    }

    public List<Buku> stokTipis(int batas) {
        return s().createQuery("from Buku b where b.stok <= :b order by b.stok", Buku.class)
                .setParameter("b", batas).list();
    }
}

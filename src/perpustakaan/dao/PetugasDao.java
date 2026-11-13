package perpustakaan.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import perpustakaan.model.Petugas;

@Repository
public class PetugasDao {
    @Autowired
    private SessionFactory sf;

    private Session s() { return sf.getCurrentSession(); }

    public Petugas login(String user, String pass) {
        return s().createQuery("from Petugas p where p.username=:u and p.password=:p", Petugas.class)
                .setParameter("u", user).setParameter("p", pass).uniqueResult();
    }

    public long hitung() {
        Long c = s().createQuery("select count(p) from Petugas p", Long.class).uniqueResult();
        return c == null ? 0 : c;
    }

    public void simpan(Petugas p) { s().persist(p); }
}

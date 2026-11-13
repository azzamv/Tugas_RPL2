package perpustakaan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perpustakaan.dao.PetugasDao;
import perpustakaan.model.Petugas;

@Service
@Transactional
public class AuthService {
    @Autowired
    private PetugasDao dao;

    public void init() {
        if (dao.hitung() == 0) {
            Petugas p = new Petugas();
            p.setUsername("admin");
            p.setPassword("admin123");
            p.setNama("Administrator");
            dao.simpan(p);
        }
    }

    public Petugas login(String user, String pass) {
        if (user == null || user.trim().isEmpty() || pass == null || pass.isEmpty())
            throw new RuntimeException("Username dan password wajib diisi");
        Petugas p = dao.login(user.trim(), pass);
        if (p == null) throw new RuntimeException("Username atau password salah");
        return p;
    }
}

package perpustakaan.model;

import jakarta.persistence.*;

@Entity
@Table(name = "buku")
public class Buku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String judul;

    private String pengarang;
    private String penerbit;
    private Integer tahun;
    private Integer stok = 0;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public String getPengarang() { return pengarang; }
    public void setPengarang(String pengarang) { this.pengarang = pengarang; }
    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }
    public Integer getTahun() { return tahun; }
    public void setTahun(Integer tahun) { this.tahun = tahun; }
    public Integer getStok() { return stok; }
    public void setStok(Integer stok) { this.stok = stok; }

    @Override
    public String toString() { return judul; }
}

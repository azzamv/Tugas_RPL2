package perpustakaan.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "peminjaman")
public class Pinjam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_anggota", nullable = false)
    private Anggota anggota;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_buku", nullable = false)
    private Buku buku;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_petugas", nullable = false)
    private Petugas petugas;

    @Column(name = "tgl_pinjam", nullable = false)
    private LocalDate tglPinjam;

    @Column(name = "tgl_kembali")
    private LocalDate tglKembali;

    @Column(nullable = false)
    private String status = "Dipinjam";

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Anggota getAnggota() { return anggota; }
    public void setAnggota(Anggota anggota) { this.anggota = anggota; }
    public Buku getBuku() { return buku; }
    public void setBuku(Buku buku) { this.buku = buku; }
    public Petugas getPetugas() { return petugas; }
    public void setPetugas(Petugas petugas) { this.petugas = petugas; }
    public LocalDate getTglPinjam() { return tglPinjam; }
    public void setTglPinjam(LocalDate tglPinjam) { this.tglPinjam = tglPinjam; }
    public LocalDate getTglKembali() { return tglKembali; }
    public void setTglKembali(LocalDate tglKembali) { this.tglKembali = tglKembali; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

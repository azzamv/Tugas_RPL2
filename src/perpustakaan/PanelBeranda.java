package perpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelBeranda extends JPanel implements Muat {

    private final JLabel[] nilai = new JLabel[3];
    private final JTable tabel = new JTable();
    private final JPanel daftarStok = new JPanel();

    public PanelBeranda() {
        setLayout(new BorderLayout(0, 20));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        // Baris atas: tiga kartu statistik
        JPanel baris = new JPanel(new GridLayout(1, 3, 20, 0));
        baris.setOpaque(false);
        baris.setPreferredSize(new Dimension(0, 120));
        baris.add(kartuStat(0, "Jumlah Buku", Tema.BIRU));
        baris.add(kartuStat(1, "Anggota Aktif", Tema.HIJAU));
        baris.add(kartuStat(2, "Transaksi Hari Ini", Tema.ORANYE));
        add(baris, BorderLayout.NORTH);

        // Baris bawah: tabel transaksi dan daftar stok menipis
        JPanel bawah = new JPanel(new BorderLayout(20, 0));
        bawah.setOpaque(false);
        bawah.add(kartuTransaksi(), BorderLayout.CENTER);
        bawah.add(kartuStok(), BorderLayout.EAST);
        add(bawah, BorderLayout.CENTER);
    }

    private Tema.Kartu kartuStat(int indeks, String judul, Color warna) {
        Tema.Kartu k = new Tema.Kartu(warna);
        k.setLayout(new BorderLayout());
        k.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(Tema.FONT_TEBAL);
        lblJudul.setForeground(Color.WHITE);

        nilai[indeks] = new JLabel("0");
        nilai[indeks].setFont(Tema.FONT_ANGKA);
        nilai[indeks].setForeground(Color.WHITE);

        k.add(lblJudul, BorderLayout.NORTH);
        k.add(nilai[indeks], BorderLayout.CENTER);
        return k;
    }

    private Tema.Kartu kartuTransaksi() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout(0, 10));
        k.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel judul = new JLabel("Transaksi Terbaru");
        judul.setFont(Tema.FONT_TEBAL);
        judul.setForeground(Tema.TEKS);

        tabel.setFont(Tema.FONT_BIASA);
        tabel.setRowHeight(32);
        tabel.setShowGrid(false);
        tabel.setIntercellSpacing(new Dimension(0, 0));
        tabel.setFillsViewportHeight(true);
        tabel.setSelectionBackground(new Color(0xDBEAFE));
        tabel.setSelectionForeground(Tema.TEKS);
        tabel.getTableHeader().setFont(Tema.FONT_TEBAL);
        tabel.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.GARIS));
        scroll.getViewport().setBackground(Tema.PUTIH);

        k.add(judul, BorderLayout.NORTH);
        k.add(scroll, BorderLayout.CENTER);
        return k;
    }

    private Tema.Kartu kartuStok() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout(0, 10));
        k.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        k.setPreferredSize(new Dimension(280, 0));

        JLabel judul = new JLabel("Stok Menipis");
        judul.setFont(Tema.FONT_TEBAL);
        judul.setForeground(Tema.TEKS);

        daftarStok.setOpaque(false);
        daftarStok.setLayout(new BoxLayout(daftarStok, BoxLayout.Y_AXIS));

        k.add(judul, BorderLayout.NORTH);
        k.add(daftarStok, BorderLayout.CENTER);
        return k;
    }

    // Satu baris di daftar stok: judul buku di kiri, sisa stok di kanan
    private JPanel barisStok(String judul, int sisa) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.GARIS),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel kiri = new JLabel(judul);
        kiri.setFont(Tema.FONT_BIASA);
        kiri.setForeground(Tema.TEKS);
        JLabel kanan = new JLabel("Sisa " + sisa);
        kanan.setFont(Tema.FONT_TEBAL);
        kanan.setForeground(Tema.ORANYE);

        p.add(kiri, BorderLayout.CENTER);
        p.add(kanan, BorderLayout.EAST);
        return p;
    }

    // Sementara: data contoh. Nanti diganti data dari database.
    @Override
    public void muat() {
        nilai[0].setText("5");
        nilai[1].setText("2");
        nilai[2].setText("1");

        String[] kolom = {"Buku", "Anggota", "Tanggal", "Status"};
        Object[][] data = {
            {"Pemrograman Java", "Andi Pratama", "2026-10-28", "Dipinjam"},
            {"Basis Data", "Siti Aminah", "2026-10-27", "Kembali"},
            {"Rekayasa Perangkat Lunak", "Andi Pratama", "2026-10-25", "Dipinjam"},
            {"Pemrograman Java", "Siti Aminah", "2026-10-24", "Kembali"}
        };
        tabel.setModel(new DefaultTableModel(data, kolom) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        // Lebar kolom (Buku paling lebar)
        int[] lebar = {190, 125, 95, 80};
        for (int i = 0; i < lebar.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }

        daftarStok.removeAll();
        daftarStok.add(barisStok("Rekayasa Perangkat Lunak", 2));
        daftarStok.add(barisStok("Basis Data", 3));
        daftarStok.revalidate();
        daftarStok.repaint();
    }
}
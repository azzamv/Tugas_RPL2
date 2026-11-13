package perpustakaan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import perpustakaan.App;
import perpustakaan.Muat;
import perpustakaan.Tema;
import perpustakaan.model.Buku;
import perpustakaan.model.Pinjam;
import perpustakaan.service.AnggotaService;
import perpustakaan.service.BukuService;
import perpustakaan.service.PinjamService;

public class BerandaPanel extends JPanel implements Muat {
    private final BukuService bukuSvc = App.get(BukuService.class);
    private final AnggotaService anggotaSvc = App.get(AnggotaService.class);
    private final PinjamService pinjamSvc = App.get(PinjamService.class);
    private final JLabel[] nilai = new JLabel[3];
    private final JTable tabel = new JTable();
    private final JPanel stokPanel = new JPanel();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BerandaPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        JPanel atas = new JPanel(new GridLayout(1, 3, 16, 0));
        atas.setOpaque(false);
        atas.setPreferredSize(new Dimension(0, 96));
        atas.add(kartu(0, "Jumlah Buku", Tema.BIRU));
        atas.add(kartu(1, "Anggota Aktif", Tema.HIJAU));
        atas.add(kartu(2, "Transaksi Hari Ini", Tema.ORANYE));
        add(atas, BorderLayout.NORTH);

        JPanel bawah = new JPanel(new GridLayout(1, 2, 16, 0));
        bawah.setOpaque(false);
        bawah.add(panelTabel());
        bawah.add(panelStok());
        add(bawah, BorderLayout.CENTER);
    }

    private Tema.Kartu kartu(int i, String judul, Color warna) {
        Tema.Kartu k = new Tema.Kartu(warna);
        k.setLayout(new BorderLayout(0, 4));
        k.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        JLabel t = new JLabel(judul);
        t.setFont(Tema.FONT_BIASA);
        t.setForeground(Tema.PUTIH);
        nilai[i] = new JLabel("0");
        nilai[i].setFont(Tema.FONT_ANGKA);
        nilai[i].setForeground(Tema.PUTIH);
        k.add(t, BorderLayout.NORTH);
        k.add(nilai[i], BorderLayout.CENTER);
        return k;
    }

    private Tema.Kartu panelTabel() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout(0, 10));
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        JLabel t = new JLabel("Transaksi Terbaru");
        t.setFont(Tema.FONT_TEBAL);
        t.setForeground(Tema.TEKS);
        Tema.styleTabel(tabel);
        tabel.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(Tema.garis());
        sp.getViewport().setBackground(Tema.PUTIH);
        k.add(t, BorderLayout.NORTH);
        k.add(sp, BorderLayout.CENTER);
        return k;
    }

    private Tema.Kartu panelStok() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout(0, 10));
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        JLabel t = new JLabel("Stok Menipis");
        t.setFont(Tema.FONT_TEBAL);
        t.setForeground(Tema.TEKS);
        stokPanel.setOpaque(false);
        stokPanel.setLayout(new BoxLayout(stokPanel, BoxLayout.Y_AXIS));
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(stokPanel, BorderLayout.NORTH);
        k.add(t, BorderLayout.NORTH);
        k.add(wrap, BorderLayout.CENTER);
        return k;
    }

    @Override
    public void muat() {
        try {
            nilai[0].setText(String.valueOf(bukuSvc.hitung()));
            nilai[1].setText(String.valueOf(anggotaSvc.hitungAktif()));
            nilai[2].setText(String.valueOf(pinjamSvc.hariIni()));

            List<Pinjam> list = pinjamSvc.terbaru();
            String[] kol = {"Buku", "Anggota", "Tanggal", "Status"};
            Object[][] data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                Pinjam p = list.get(i);
                data[i][0] = p.getBuku() != null ? p.getBuku().getJudul() : "-";
                data[i][1] = p.getAnggota() != null ? p.getAnggota().getNama() : "-";
                data[i][2] = p.getTglPinjam() != null ? p.getTglPinjam().format(fmt) : "-";
                data[i][3] = p.getStatus();
            }
            tabel.setModel(new DefaultTableModel(data, kol) {
                public boolean isCellEditable(int r, int c) { return false; }
            });

            stokPanel.removeAll();
            List<Buku> tipis = bukuSvc.stokTipis();
            if (tipis.isEmpty()) {
                JLabel l = new JLabel("Semua stok aman");
                l.setFont(Tema.FONT_BIASA);
                l.setForeground(Tema.TEKS_PUDAR);
                stokPanel.add(l);
            } else {
                for (Buku b : tipis) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setOpaque(false);
                    row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.GARIS));
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                    JLabel kiri = new JLabel(b.getJudul());
                    kiri.setFont(Tema.FONT_BIASA);
                    kiri.setForeground(Tema.TEKS);
                    kiri.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 8));
                    JLabel kanan = new JLabel("sisa " + b.getStok());
                    kanan.setFont(Tema.FONT_TEBAL);
                    kanan.setForeground(Tema.ORANYE);
                    kanan.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
                    row.add(kiri, BorderLayout.CENTER);
                    row.add(kanan, BorderLayout.EAST);
                    stokPanel.add(row);
                }
            }
            stokPanel.revalidate();
            stokPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal muat: " + e.getMessage());
        }
    }
}

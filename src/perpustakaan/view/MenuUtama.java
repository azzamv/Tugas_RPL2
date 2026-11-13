package perpustakaan.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import perpustakaan.App;
import perpustakaan.Muat;
import perpustakaan.Tema;
import perpustakaan.model.Petugas;

public class MenuUtama extends JFrame {
    private final CardLayout kartu = new CardLayout();
    private final JPanel isi = new JPanel(kartu);
    private final JPanel sidebar = new JPanel();
    private final JLabel judul = new JLabel("Beranda");
    private final java.util.List<JLabel> menu = new ArrayList<>();
    private final Petugas petugas;

    public MenuUtama(Petugas p) {
        super("Perpustakaan");
        this.petugas = p;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { keluar(); } });
        setSize(1050, 640);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);

        buatSidebar();
        BerandaPanel beranda = new BerandaPanel();
        tambah("Beranda", beranda);
        tambah("Data Buku", new BukuPanel());
        tambah("Data Anggota", new AnggotaPanel());
        tambah("Transaksi", new PinjamPanel(petugas));
        tambah("Laporan", kosong("Laporan"));

        sidebar.add(Box.createVerticalGlue());
        JLabel out = item("Keluar");
        out.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { keluar(); } });
        sidebar.add(out);
        sidebar.add(Box.createVerticalStrut(16));

        JPanel kanan = new JPanel(new BorderLayout());
        kanan.add(header(petugas != null ? petugas.getNama() : "-"), BorderLayout.NORTH);
        isi.setBackground(Tema.LATAR);
        kanan.add(isi, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);
        add(kanan, BorderLayout.CENTER);
        buka("Beranda", beranda, menu.get(0));
    }

    private void keluar() {
        try { if (App.ctx != null) App.ctx.close(); } catch (Exception ignored) {}
        dispose();
        System.exit(0);
    }

    private void buatSidebar() {
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Tema.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 0, 16));

        JLabel t = new JLabel("Perpustakaan");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setForeground(Tema.PUTIH);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel s = new JLabel("Sistem Informasi");
        s.setFont(Tema.FONT_BIASA);
        s.setForeground(Tema.SUB_SIDEBAR);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(t);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(s);
        sidebar.add(Box.createVerticalStrut(24));
    }

    private JPanel header(String user) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Tema.PUTIH);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.GARIS),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));
        judul.setFont(Tema.FONT_JUDUL);
        judul.setForeground(Tema.TEKS);
        String tgl = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID")));
        JLabel info = new JLabel(user + "  ·  " + tgl);
        info.setFont(Tema.FONT_BIASA);
        info.setForeground(Tema.TEKS_PUDAR);
        h.add(judul, BorderLayout.WEST);
        h.add(info, BorderLayout.EAST);
        return h;
    }

    private void tambah(String nama, JPanel p) {
        isi.add(p, nama);
        JLabel m = item(nama);
        m.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { buka(nama, p, m); } });
        sidebar.add(m);
        sidebar.add(Box.createVerticalStrut(4));
        menu.add(m);
    }

    private JLabel item(String teks) {
        JLabel l = new JLabel(teks);
        l.setOpaque(true);
        l.setFont(Tema.FONT_BIASA);
        l.setForeground(Tema.PUTIH);
        l.setBackground(Tema.SIDEBAR);
        l.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        l.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (l.getBackground() != Tema.BIRU) l.setBackground(Tema.SIDEBAR_HOVER); }
            public void mouseExited(MouseEvent e) { if (l.getBackground() != Tema.BIRU) l.setBackground(Tema.SIDEBAR); }
        });
        return l;
    }

    private void buka(String nama, JPanel p, JLabel m) {
        for (JLabel x : menu) x.setBackground(Tema.SIDEBAR);
        m.setBackground(Tema.BIRU);
        if (p instanceof Muat) ((Muat) p).muat();
        judul.setText(nama);
        kartu.show(isi, nama);
    }

    private JPanel kosong(String nama) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.LATAR);
        JLabel t = new JLabel("Halaman " + nama + " belum tersedia");
        t.setFont(Tema.FONT_BIASA);
        t.setForeground(Tema.TEKS_PUDAR);
        p.add(t);
        return p;
    }
}

package perpustakaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Dashboard extends JFrame {

    private final CardLayout kartu = new CardLayout();
    private final JPanel isi = new JPanel(kartu);
    private final JPanel sidebar = new JPanel();
    private final JLabel judulHalaman = new JLabel("Beranda");
    private final ArrayList<MenuItem> daftarMenu = new ArrayList<>();

    public Dashboard(String user) {
        super("Aplikasi Perpustakaan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        buatSidebar();

        PanelBeranda beranda = new PanelBeranda();
        tambahMenu("Beranda", beranda);
        tambahMenu("Data Buku", halamanSementara("Data Buku"));
        tambahMenu("Data Anggota", halamanSementara("Data Anggota"));
        tambahMenu("Transaksi", halamanSementara("Transaksi"));
        tambahMenu("Laporan", halamanSementara("Laporan"));

        // Tombol keluar di bagian bawah sidebar
        sidebar.add(Box.createVerticalGlue());
        MenuItem keluar = new MenuItem("Keluar");
        keluar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        sidebar.add(keluar);
        sidebar.add(Box.createVerticalStrut(15));

        // Sisi kanan: header di atas, isi halaman di bawah
        JPanel kanan = new JPanel(new BorderLayout());
        kanan.add(buatHeader(user), BorderLayout.NORTH);
        isi.setBackground(Tema.LATAR);
        kanan.add(isi, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(kanan, BorderLayout.CENTER);

        buka("Beranda", beranda, daftarMenu.get(0));
    }

    private void buatSidebar() {
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Tema.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));

        JLabel judul = new JLabel("PERPUSTAKAAN");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);
        judul.setBorder(BorderFactory.createEmptyBorder(30, 25, 2, 10));

        JLabel sub = new JLabel("Sistem Informasi");
        sub.setFont(Tema.FONT_BIASA);
        sub.setForeground(new Color(0x94A3B8));
        sub.setBorder(BorderFactory.createEmptyBorder(0, 25, 30, 10));

        sidebar.add(judul);
        sidebar.add(sub);
    }

    private JPanel buatHeader(String user) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Tema.PUTIH);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.GARIS),
                BorderFactory.createEmptyBorder(16, 25, 16, 25)));

        judulHalaman.setFont(Tema.FONT_JUDUL);
        judulHalaman.setForeground(Tema.TEKS);

        String tanggal = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.forLanguageTag("id-ID")));
        JLabel info = new JLabel("Petugas: " + user + "   |   " + tanggal);
        info.setFont(Tema.FONT_BIASA);
        info.setForeground(Tema.TEKS_PUDAR);

        header.add(judulHalaman, BorderLayout.WEST);
        header.add(info, BorderLayout.EAST);
        return header;
    }

    // Mendaftarkan satu halaman dan membuat item menunya
    private void tambahMenu(String nama, JPanel panel) {
        isi.add(panel, nama);
        MenuItem m = new MenuItem(nama);
        m.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buka(nama, panel, m);
            }
        });
        sidebar.add(m);
        daftarMenu.add(m);
    }

    // Menampilkan halaman dan menandai menu yang aktif
    private void buka(String nama, JPanel panel, MenuItem m) {
        for (MenuItem x : daftarMenu) {
            x.setAktif(false);
        }
        m.setAktif(true);
        if (panel instanceof Muat) {
            ((Muat) panel).muat();
        }
        judulHalaman.setText(nama);
        kartu.show(isi, nama);
    }

    // Halaman kosong sementara. Nanti diganti panel asli.
    private JPanel halamanSementara(String nama) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Tema.LATAR);
        JLabel teks = new JLabel("Halaman " + nama + " belum dibuat", SwingConstants.CENTER);
        teks.setFont(Tema.FONT_JUDUL);
        teks.setForeground(Tema.TEKS_PUDAR);
        p.add(teks);
        return p;
    }

    // Item menu di sidebar. Memakai JLabel agar warnanya sama di semua Windows.
    private static class MenuItem extends JLabel {

        private boolean aktif;

        MenuItem(String teks) {
            super(teks);
            setOpaque(true);
            setFont(Tema.FONT_BIASA);
            setForeground(Color.WHITE);
            setBackground(Tema.SIDEBAR);
            setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 10));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!aktif) {
                        setBackground(Tema.SIDEBAR_HOVER);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!aktif) {
                        setBackground(Tema.SIDEBAR);
                    }
                }
            });
        }

        void setAktif(boolean aktif) {
            this.aktif = aktif;
            setBackground(aktif ? Tema.BIRU : Tema.SIDEBAR);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new Dashboard("admin").setVisible(true));
    }
}
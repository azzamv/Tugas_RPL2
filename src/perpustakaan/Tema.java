package perpustakaan;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;

// Tema hangat harmonis (sand & clay) + util UI simpel
public class Tema {

    public static final Color SIDEBAR = new Color(0x4B4238);
    public static final Color SIDEBAR_HOVER = new Color(0x5A5045);
    public static final Color LATAR = new Color(0xF6F1EA);
    public static final Color GARIS = new Color(0xE4DDD4);
    public static final Color PUTIH = new Color(0xFFFCF8);
    public static final Color TEKS = new Color(0x332E29);
    public static final Color TEKS_PUDAR = new Color(0x81786F);
    public static final Color BIRU = new Color(0xB97A56);    // aksen utama
    public static final Color HIJAU = new Color(0x8C9A7A);   // sekunder
    public static final Color ORANYE = new Color(0xC9A66B);  // peringatan hangat
    public static final Color SUB_SIDEBAR = new Color(0xCFC6BC);
    public static final Color SALAH = new Color(0xB85C4F);
    public static final Color AKSEN_GELAP = new Color(0xA56A49);
    public static final Color TABEL_HEADER = new Color(0xF3EEE7);
    public static final Color TABEL_PILIH = new Color(0xEFE6DC);

    public static final Font FONT_BIASA = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_TEBAL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_JUDUL = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_ANGKA = new Font("Segoe UI", Font.BOLD, 34);

    public static Border garis() {
        return BorderFactory.createLineBorder(GARIS);
    }

    public static void styleField(JTextField f) {
        f.setFont(FONT_BIASA);
        f.setForeground(TEKS);
        f.setBackground(PUTIH);
        f.setCaretColor(TEKS);
        f.setBorder(BorderFactory.createCompoundBorder(garis(),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    public static void styleTabel(JTable t) {
        t.setFont(FONT_BIASA);
        t.setForeground(TEKS);
        t.setRowHeight(30);
        t.setShowVerticalLines(false);
        t.setGridColor(GARIS);
        t.setSelectionBackground(TABEL_PILIH);
        t.setSelectionForeground(TEKS);
        t.setBackground(PUTIH);
        JTableHeader h = t.getTableHeader();
        h.setFont(FONT_TEBAL);
        h.setBackground(TABEL_HEADER);
        h.setForeground(TEKS);
        h.setBorder(garis());
        h.setReorderingAllowed(false);
    }

    public static JButton tombol(String teks) {
        return tombol(teks, BIRU, AKSEN_GELAP, PUTIH);
    }

    public static JButton tombolNetral(String teks) {
        return tombol(teks, new Color(0xE9E2D9), new Color(0xDED6CB), TEKS);
    }

    private static JButton tombol(String teks, Color bg, Color hover, Color fg) {
        JButton b = new JButton(teks) {
            boolean h;
            {
                setFont(FONT_TEBAL);
                setForeground(fg);
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { h = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e) { h = false; repaint(); }
                });
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(h ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        return b;
    }

    // Panel dengan sudut membulat
    public static class Kartu extends JPanel {
        private final Color warna;

        public Kartu(Color warna) {
            this.warna = warna;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(warna);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
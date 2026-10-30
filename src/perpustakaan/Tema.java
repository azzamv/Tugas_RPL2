package perpustakaan;

import java.awt.*;
import javax.swing.*;

// Warna, font, dan komponen tampilan yang dipakai semua halaman
public class Tema {

    public static final Color SIDEBAR = new Color(0x1E293B);
    public static final Color SIDEBAR_HOVER = new Color(0x334155);
    public static final Color LATAR = new Color(0xF1F5F9);
    public static final Color GARIS = new Color(0xE2E8F0);
    public static final Color PUTIH = Color.WHITE;
    public static final Color TEKS = new Color(0x0F172A);
    public static final Color TEKS_PUDAR = new Color(0x64748B);
    public static final Color BIRU = new Color(0x3B82F6);
    public static final Color HIJAU = new Color(0x10B981);
    public static final Color ORANYE = new Color(0xF59E0B);

    public static final Font FONT_BIASA = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_TEBAL = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_JUDUL = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_ANGKA = new Font("Segoe UI", Font.BOLD, 42);

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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
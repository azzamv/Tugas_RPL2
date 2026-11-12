package perpustakaan;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private final JTextField isiUser = new JTextField();
    private final JPasswordField isiSandi = new JPasswordField();
    private final JLabel pesan = new JLabel(" ");

    public Login() {
        super("Login Perpustakaan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridLayout(1, 2));
        add(buatPanelKiri());
        add(buatPanelForm());

        // Tekan Enter di mana saja untuk masuk
        getRootPane().setDefaultButton(null);
        isiUser.addActionListener(e -> isiSandi.requestFocusInWindow());
        isiSandi.addActionListener(e -> proses());
    }

    private JPanel buatPanelKiri() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Tema.SIDEBAR);
        p.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel judul = new JLabel("PERPUSTAKAAN");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Sistem Informasi");
        sub.setFont(Tema.FONT_BIASA);
        sub.setForeground(new Color(0x94A3B8));

        p.add(Box.createVerticalGlue());
        p.add(judul);
        p.add(Box.createVerticalStrut(6));
        p.add(sub);
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel buatPanelForm() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Tema.PUTIH);
        p.setBorder(BorderFactory.createEmptyBorder(0, 45, 0, 45));

        JLabel judul = new JLabel("Masuk");
        judul.setFont(Tema.FONT_JUDUL);
        judul.setForeground(Tema.TEKS);

        JLabel petunjuk = new JLabel("Masukkan akun petugas Anda");
        petunjuk.setFont(Tema.FONT_BIASA);
        petunjuk.setForeground(Tema.TEKS_PUDAR);

        pesan.setFont(Tema.FONT_BIASA);
        pesan.setForeground(new Color(0xEF4444));

        Tombol masuk = new Tombol("Masuk");
        masuk.addActionListener(e -> proses());

        p.add(Box.createVerticalGlue());
        p.add(rataKiri(judul));
        p.add(Box.createVerticalStrut(4));
        p.add(rataKiri(petunjuk));
        p.add(Box.createVerticalStrut(25));
        p.add(rataKiri(label("Username")));
        p.add(Box.createVerticalStrut(6));
        p.add(rataKiri(gayaInput(isiUser)));
        p.add(Box.createVerticalStrut(15));
        p.add(rataKiri(label("Password")));
        p.add(Box.createVerticalStrut(6));
        p.add(rataKiri(gayaInput(isiSandi)));
        p.add(Box.createVerticalStrut(8));
        p.add(rataKiri(pesan));
        p.add(Box.createVerticalStrut(10));
        p.add(rataKiri(masuk));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JLabel label(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(Tema.FONT_TEBAL);
        l.setForeground(Tema.TEKS);
        return l;
    }

    private JComponent gayaInput(JTextField f) {
        f.setFont(Tema.FONT_BIASA);
        f.setForeground(Tema.TEKS);
        f.setBackground(Tema.PUTIH);
        f.setCaretColor(Tema.TEKS);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.GARIS),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return f;
    }

    private JComponent rataKiri(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private void proses() {
        String user = isiUser.getText().trim();
        String sandi = new String(isiSandi.getPassword());

        if (user.isEmpty() || sandi.isEmpty()) {
            pesan.setText("Username dan password wajib diisi");
            return;
        }
        if (!cekLogin(user, sandi)) {
            pesan.setText("Username atau password salah");
            isiSandi.setText("");
            isiSandi.requestFocusInWindow();
            return;
        }

        dispose();
        new Dashboard(user).setVisible(true);
    }

    // Sementara: akun contoh. Nanti ganti dengan query ke database lewat ORM.
    private boolean cekLogin(String user, String sandi) {
        return user.equals("admin") && sandi.equals("admin123");
    }

    // Tombol berwarna. JButton bawaan Windows mengabaikan warna latar.
    private static class Tombol extends JButton {

        private boolean hover;

        Tombol(String teks) {
            super(teks);
            setFont(Tema.FONT_TEBAL);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            setPreferredSize(new Dimension(100, 42));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? new Color(0x2563EB) : Tema.BIRU);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
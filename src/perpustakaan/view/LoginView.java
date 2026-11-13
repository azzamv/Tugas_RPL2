package perpustakaan.view;

import javax.swing.*;
import java.awt.*;
import perpustakaan.App;
import perpustakaan.Tema;
import perpustakaan.model.Petugas;
import perpustakaan.service.AuthService;

public class LoginView extends JFrame {
    private final JTextField txtUser = new JTextField();
    private final JPasswordField txtPass = new JPasswordField();
    private final JLabel info = new JLabel(" ");
    private final AuthService auth = App.get(AuthService.class);

    public LoginView() {
        super("Login Perpustakaan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));
        add(kiri());
        add(form());
        txtUser.addActionListener(e -> txtPass.requestFocusInWindow());
        txtPass.addActionListener(e -> masuk());
    }

    private JPanel kiri() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.SIDEBAR);
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        JLabel j1 = new JLabel("Perpustakaan");
        j1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        j1.setForeground(Tema.PUTIH);
        j1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel j2 = new JLabel("Sistem Informasi");
        j2.setFont(Tema.FONT_BIASA);
        j2.setForeground(Tema.SUB_SIDEBAR);
        j2.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(j1);
        box.add(Box.createVerticalStrut(6));
        box.add(j2);
        p.add(box);
        return p;
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Tema.PUTIH);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        c.insets = new Insets(4, 48, 4, 48);

        JLabel judul = new JLabel("Masuk");
        judul.setFont(Tema.FONT_JUDUL);
        judul.setForeground(Tema.TEKS);
        JLabel ket = new JLabel("Gunakan akun petugas");
        ket.setFont(Tema.FONT_BIASA);
        ket.setForeground(Tema.TEKS_PUDAR);
        info.setFont(Tema.FONT_BIASA);
        info.setForeground(Tema.SALAH);

        Tema.styleField(txtUser);
        Tema.styleField(txtPass);
        JButton btn = Tema.tombol("Masuk");
        btn.addActionListener(e -> masuk());

        c.gridy = 0; c.insets = new Insets(0,48,2,48); p.add(judul, c);
        c.gridy = 1; c.insets = new Insets(0,48,18,48); p.add(ket, c);
        c.gridy = 2; c.insets = new Insets(4,48,4,48); p.add(label("Username"), c);
        c.gridy = 3; p.add(txtUser, c);
        c.gridy = 4; c.insets = new Insets(12,48,4,48); p.add(label("Password"), c);
        c.gridy = 5; c.insets = new Insets(4,48,4,48); p.add(txtPass, c);
        c.gridy = 6; c.insets = new Insets(6,48,10,48); p.add(info, c);
        c.gridy = 7; c.insets = new Insets(4,48,0,48); p.add(btn, c);
        return p;
    }

    private void masuk() {
        try {
            Petugas p = auth.login(txtUser.getText(), new String(txtPass.getPassword()));
            dispose();
            new MenuUtama(p).setVisible(true);
        } catch (Exception e) {
            info.setText(e.getMessage());
            txtPass.setText("");
        }
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(Tema.FONT_TEBAL);
        l.setForeground(Tema.TEKS);
        return l;
    }
}

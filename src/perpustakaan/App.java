package perpustakaan;

import javax.swing.*;
import java.awt.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import perpustakaan.service.AuthService;
import perpustakaan.view.LoginView;

public class App {
    public static ClassPathXmlApplicationContext ctx;

    public static <T> T get(Class<T> c) { return ctx.getBean(c); }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        JWindow splash = new JWindow();
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(0xF6F1EA));
        p.setBorder(BorderFactory.createLineBorder(new Color(0xE4DDD4)));
        JLabel l = new JLabel("Memuat aplikasi...", SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(new Color(0x332E29));
        l.setBorder(BorderFactory.createEmptyBorder(20, 36, 20, 36));
        p.add(l);
        splash.setContentPane(p);
        splash.pack();
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() {
                try {
                    ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
                    App.get(AuthService.class).init();
                } catch (Exception e) {
                    err = e;
                }
                return null;
            }
            protected void done() {
                splash.dispose();
                if (err != null) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal memuat: " + err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } else {
                    new LoginView().setVisible(true);
                }
            }
        }.execute();
    }
}

package perpustakaan;

import javax.swing.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import perpustakaan.service.AuthService;
import perpustakaan.view.LoginView;

public class App {
    public static ClassPathXmlApplicationContext ctx;

    public static <T> T get(Class<T> c) { return ctx.getBean(c); }

    public static void main(String[] args) {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        try {
            get(AuthService.class).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}

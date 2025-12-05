import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("Login - Sistema de Garagem");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 8, 8));

        JLabel lblUser = new JLabel("Usuário:");
        JLabel lblPass = new JLabel("Senha:");
        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Entrar");

        add(lblUser); add(txtUser);
        add(lblPass); add(txtPass);
        add(new JLabel("")); add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {
                new TelaGaragem();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login inválido! Usuário/senha incorretos.");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

package minimarket.consola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciar;

    public LoginFrame() {
        setTitle("Inicio de Sesión - Minimarket");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        btnIniciar = new JButton("Iniciar sesión");

        add(lblUsuario);
        add(txtUsuario);
        add(lblContrasena);
        add(txtContrasena);
        add(new JLabel()); // Espacio vacío
        add(btnIniciar);

        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.equals("admin") && contrasena.equals("123")) {
            JOptionPane.showMessageDialog(this, "Bienvenido, Administrador!");
            dispose();
            MainApp.main(new String[]{"admin"});
        } else if (usuario.equals("empleado") && contrasena.equals("123")) {
            JOptionPane.showMessageDialog(this, "Bienvenido, Empleado!");
            dispose();
            MainApp.main(new String[]{"empleado"});
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}

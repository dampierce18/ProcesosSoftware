package minimarket.consola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import minimarket.controlador.ControladorPrincipal;
import minimarket.negocio.Inventario;
import minimarket.registro.RegistroVentas;

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
        Inventario inventario = new Inventario();
        RegistroVentas registroVentas = new RegistroVentas();
        ControladorPrincipal controlador = new ControladorPrincipal(inventario, registroVentas);
        new ventanas.MenuPrincipal(controlador).setVisible(true);
    }
    
    if (usuario.equals("empleado") && contrasena.equals("123")) {
        JOptionPane.showMessageDialog(this, "Bienvenido, Empleado!");
        dispose();
        Inventario inventario = new Inventario();
        RegistroVentas registroVentas = new RegistroVentas();
        ControladorPrincipal controlador = new ControladorPrincipal(inventario, registroVentas);
        new ventanas.MenuPrincipal(controlador).setVisible(true);
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

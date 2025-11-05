package ventanas;

import ventanas.MenuPrincipal;
import javax.swing.*;
import java.awt.*;

public class VentanaRegistroVentas extends JFrame {

    public VentanaRegistroVentas() {
        setTitle("Registro de Ventas");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // 🩶 Panel de título "Registro de ventas"
        JLabel lblTitulo = new JLabel("Registro de Ventas", SwingConstants.CENTER);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(220, 220, 220));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(300, 70, 350, 30);
        add(lblTitulo);

        // 🧾 Campos de texto (superiores)
        JTextField txt1 = new JTextField();
        txt1.setBounds(50, 120, 180, 25);
        add(txt1);

        JTextField txt2 = new JTextField();
        txt2.setBounds(240, 120, 180, 25);
        add(txt2);

        JTextField txt3 = new JTextField();
        txt3.setBounds(530, 120, 180, 25);
        add(txt3);

        JTextField txt4 = new JTextField();
        txt4.setBounds(720, 120, 180, 25);
        add(txt4);

        JTextField txt5 = new JTextField();
        txt5.setBounds(50, 160, 180, 25);
        add(txt5);

        JTextField txt6 = new JTextField();
        txt6.setBounds(240, 160, 180, 25);
        add(txt6);

        JTextField txt7 = new JTextField();
        txt7.setBounds(530, 160, 180, 25);
        add(txt7);

        JTextField txt8 = new JTextField();
        txt8.setBounds(720, 160, 180, 25);
        add(txt8);

        // 🧮 Tablas para Caja 1, Caja 2, Caja 3
        String[] columnas = {"Producto", "Cantidad", "Precio"};
        Object[][] datosVacios = {
                {"", "", ""},
                {"", "", ""},
                {"", "", ""},
                {"", "", ""},
        };

        JTable tabla1 = new JTable(datosVacios, columnas);
        JTable tabla2 = new JTable(datosVacios, columnas);
        JTable tabla3 = new JTable(datosVacios, columnas);

        JScrollPane scroll1 = new JScrollPane(tabla1);
        JScrollPane scroll2 = new JScrollPane(tabla2);
        JScrollPane scroll3 = new JScrollPane(tabla3);

        scroll1.setBounds(50, 230, 260, 200);
        scroll2.setBounds(340, 230, 260, 200);
        scroll3.setBounds(630, 230, 260, 200);

        add(scroll1);
        add(scroll2);
        add(scroll3);

        // Etiquetas debajo de cada tabla
        JLabel lblCaja1 = new JLabel("Caja 1", SwingConstants.CENTER);
        lblCaja1.setBounds(50, 440, 260, 25);
        add(lblCaja1);

        JLabel lblCaja2 = new JLabel("Caja 2", SwingConstants.CENTER);
        lblCaja2.setBounds(340, 440, 260, 25);
        add(lblCaja2);

        JLabel lblCaja3 = new JLabel("Caja 3", SwingConstants.CENTER);
        lblCaja3.setBounds(630, 440, 260, 25);
        add(lblCaja3);
        
        JButton btnVolver = new JButton("Volver al Menú Principal");
        btnVolver.setBounds(370, 480, 200, 30);
        btnVolver.addActionListener(e -> {
            dispose(); // Cierra esta ventana
        });
        add(btnVolver);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaRegistroVentas().setVisible(true);
        });
    }
}

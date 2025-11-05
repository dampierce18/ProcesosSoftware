package ventanas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

public class VentanaRegistroPedidos extends JFrame {

    public VentanaRegistroPedidos() {
        setTitle("Registro de Pedidos");
        setSize(800, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Panel superior con ícono y título
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Ícono (puedes cambiar a una imagen si deseas)
        JLabel iconLabel = new JLabel("🌐", JLabel.CENTER);
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        topPanel.add(iconLabel, BorderLayout.NORTH);

        // Título con fondo gris claro
        JLabel titleLabel = new JLabel("Registro de Pedidos", JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panel de campos (2 filas x 4 columnas)
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Agrega 8 campos de texto (4 por fila)
        for (int i = 1; i <= 8; i++) {
            fieldsPanel.add(createLabeledField("Campo " + i));
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(fieldsPanel, BorderLayout.NORTH);

        // Tabla inferior
        String[] columnNames = {"ID Pedido", "Cliente", "Fecha", "Estado"};
        Object[][] data = {
                {"001", "Juan Pérez", "2025-10-01", "Pendiente"},
                {"002", "Ana Torres", "2025-10-02", "Entregado"},
                {"003", "Carlos Ruiz", "2025-10-03", "Cancelado"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columnNames));

        // Personalizar encabezado de tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane tableScroll = new JScrollPane(table);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnVolver = new JButton("Volver al Menú Principal");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVolver.setBackground(new Color(220, 220, 220));
        btnVolver.setFocusPainted(false);

        // Acción del botón
        btnVolver.addActionListener(e -> {
            dispose(); // cierra la ventana actual
        });

        bottomPanel.add(btnVolver);
        add(bottomPanel, BorderLayout.PAGE_END);
    }

    private JPanel createLabeledField(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText);
        JTextField field = new JTextField();
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaRegistroPedidos().setVisible(true));
    }
}
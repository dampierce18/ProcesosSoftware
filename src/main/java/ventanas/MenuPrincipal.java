package ventanas;

import javax.swing.*;

import minimarket.controlador.ControladorPrincipal;
import minimarket.modelo.DetalleVenta;
import minimarket.modelo.Producto;
import minimarket.negocio.Inventario;
import minimarket.registro.RegistroVentas;

import java.awt.*;
import java.util.List;

public class MenuPrincipal extends JFrame {

    // Paleta de colores vino/guinda
    private final Color COLOR_FONDO = new Color(245, 240, 245); // Fondo claro con tono lavanda suave
    private final Color COLOR_PRIMARIO = new Color(120, 20, 40);       // Vino oscuro principal
    private final Color COLOR_SECUNDARIO = new Color(150, 40, 60);     // Vino medio
    private final Color COLOR_TERCIARIO = new Color(180, 60, 80);      // Vino claro
    private final Color COLOR_ACENTO = new Color(200, 80, 100);        // Acento suave
    private final Color COLOR_TEXTO_CLARO = new Color(250, 230, 230);  // Texto claro sobre fondos oscuros
    private final Color COLOR_PANEL = new Color(255, 250, 250);        // Paneles blancos con tono cálido
    private Inventario inventario;
    
    private ControladorPrincipal controlador;
    private JPanel panelIconos;
    private JTextArea listaTextos;
    private JLabel lblTotal;
    
    public MenuPrincipal(ControladorPrincipal controlador) {
    	this.controlador = controlador;
        controlador.setVista(this);
        inicializarInterfaz();
    }
    private void inicializarInterfaz() {
        setTitle("Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Fondo con tono lavanda suave
        getContentPane().setBackground(COLOR_FONDO);

        // Obtener dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // CALCULAR DIMENSIONES
        int marginLeft = 50;
        int marginTop = 90;
        int marginBottom = 160;
        int gapBetweenPanels = 30;
        
        int panelWidth = (screenWidth - 2 * marginLeft - gapBetweenPanels);
        int leftPanelWidth = (int)(panelWidth * 0.62);
        int rightPanelWidth = (int)(panelWidth * 0.35);
        int panelHeight = screenHeight - marginTop - marginBottom;
        
        //Cargar Elementos
        configurarBarraSuperior();
        configurarPanelProductos();
        configurarPanelLista();
        configurarPanelInferior();
        
        //Cargar productos del controlador
        cargarProductos();
    }

    private void cargarProductos() {
        panelIconos.removeAll();
        
        for (Producto producto : controlador.obtenerProductos()) {
            JButton boton = crearBotonProducto(producto);
            panelIconos.add(boton);
        }
        
        panelIconos.revalidate();
        panelIconos.repaint();
    }
    
    private JButton crearBotonProducto(Producto producto) {
        String textoBoton = String.format("<html><center>📦<br><b>%s</b><br>S/ %.2f</center></html>", 
            producto.getNombre(), producto.getPrecio());
        
        JButton boton = new JButton(textoBoton);
        boton.setPreferredSize(new Dimension(140, 200));
        boton.setBackground(COLOR_TERCIARIO);
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 1));
        
        if (producto.getStock() <= 0) {
            boton.setEnabled(false);
            boton.setBackground(Color.GRAY);
            boton.setText(String.format("<html><center>📦<br><b>%s</b><br>S/ %.2f<br><font color='red'>SIN STOCK</font></center></html>", 
                producto.getNombre(), producto.getPrecio()));
        }
        
        boton.addActionListener(e -> mostrarDialogoCantidad(producto));
        return boton;
    }
    private void mostrarDialogoCantidad(Producto producto) {
    	// Crear un panel personalizado para el diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        
        // Título
        JLabel lblTitulo = new JLabel("Seleccionar cantidad para:", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Nombre del producto
        JLabel lblProducto = new JLabel(producto.getNombre(), SwingConstants.CENTER);
        lblProducto.setFont(new Font("Arial", Font.BOLD, 16));
        lblProducto.setForeground(COLOR_SECUNDARIO);
        panel.add(lblProducto, BorderLayout.CENTER);
        
        // Información adicional del producto
        JLabel lblInfo = new JLabel(
            String.format("Precio: S/ %.2f | Stock disponible: %d", 
                producto.getPrecio(), producto.getStock()), 
            SwingConstants.CENTER
        );
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_PRIMARIO);
        
        // Panel para controles de cantidad
        JPanel panelCantidad = new JPanel(new FlowLayout());
        panelCantidad.setBackground(COLOR_PANEL);
        
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setForeground(COLOR_PRIMARIO);
        
        // Spinner con límite basado en el stock real
        JSpinner spinnerCantidad = new JSpinner(
            new SpinnerNumberModel(1, 1, producto.getStock(), 1)
        );
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinnerCantidad.getEditor();
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinnerCantidad.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelCantidad.add(lblCantidad);
        panelCantidad.add(spinnerCantidad);
        
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(COLOR_PANEL);
        panelContenido.add(lblInfo, BorderLayout.NORTH);
        panelContenido.add(panelCantidad, BorderLayout.CENTER);
        
        panel.add(panelContenido, BorderLayout.SOUTH);
        
        int resultado = JOptionPane.showOptionDialog(
                this,
                panel,
                "Seleccionar Cantidad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Cancelar", "Agregar al Carrito"},
                "Agregar al Carrito"
            );
        
        if (resultado == 1) {
            int cantidad = (int) spinnerCantidad.getValue();
            if (controlador.validarStock(producto, cantidad)) {
                controlador.agregarAlCarrito(producto, cantidad);
                actualizarCarrito();
                mostrarMensajeExito(producto, cantidad);
            } else {
                JOptionPane.showMessageDialog(this, "Stock insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarMensajeExito(Producto producto, int cantidad) {
        String mensaje = String.format(
            "✅ Producto agregado:\n%s\nCantidad: %d\nPrecio unitario: S/ %.2f\nSubtotal: S/ %.2f",
            producto.getNombre(), cantidad, producto.getPrecio(), cantidad * producto.getPrecio()
        );
        
        JOptionPane.showMessageDialog(this, mensaje, "Producto Agregado", JOptionPane.INFORMATION_MESSAGE);
    }
	private void actualizarCarrito() {
        StringBuilder sb = new StringBuilder();
        List<DetalleVenta> carrito = controlador.getCarrito();
        
        for (int i = 0; i < carrito.size(); i++) {
            DetalleVenta detalle = carrito.get(i);
            sb.append(String.format("%2d. %-25s %2d x S/%-6.2f %7.2f%n", 
                i + 1, 
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getProducto().getPrecio(),
                detalle.getSubtotal()
            ));
        }
        
        listaTextos.setText(sb.toString());
        lblTotal.setText(String.format("Total: S/ %.2f", controlador.calcularTotalCarrito()));
    }
	private void configurarPanelInferior() {
    	// Obtener dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    	// CALCULAR DIMENSIONES
        int marginLeft = 50;
        int marginTop = 90;
        int marginBottom = 160;
        int gapBetweenPanels = 30;
        
        int panelWidth = (screenWidth - 2 * marginLeft - gapBetweenPanels);
        int leftPanelWidth = (int)(panelWidth * 0.62);
        int rightPanelWidth = (int)(panelWidth * 0.35);
        int panelHeight = screenHeight - marginTop - marginBottom;
    	 // ⚙️ Panel inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15));
        panelInferior.setBounds(marginLeft, screenHeight - 185, screenWidth - 3 * marginLeft, 100);
        panelInferior.setBackground(COLOR_PANEL);
        panelInferior.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 2));
        add(panelInferior);

        // Botones inferiores
        String[] iconos = {"Ventas", "Pedidos", "Inventario"};
        JButton[] botonesInferiores = new JButton[3];
        
        for (int i = 0; i < iconos.length; i++) {
            JButton boton = crearBotonInferior(iconos[i]);
            panelInferior.add(boton);
            botonesInferiores[i] = boton;
        }

        // Acciones de botones inferiores
        botonesInferiores[0].addActionListener(e -> {
            new VentanaRegistroVentas().setVisible(true);
        });

        botonesInferiores[1].addActionListener(e -> {
            new VentanaRegistroPedidos().setVisible(true);
        });

        botonesInferiores[2].addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad de Inventario en desarrollo", "Inventario", JOptionPane.INFORMATION_MESSAGE);
        });
	}
	private void configurarPanelLista() {
    	// Obtener dimensiones de la pantalla
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    	// CALCULAR DIMENSIONES
        int marginLeft = 50;
        int marginTop = 90;
        int marginBottom = 160;
        int gapBetweenPanels = 30;
        
        int panelWidth = (screenWidth - 2 * marginLeft - gapBetweenPanels);
        int leftPanelWidth = (int)(panelWidth * 0.62);
        int rightPanelWidth = (int)(panelWidth * 0.35);
        int panelHeight = screenHeight - marginTop - marginBottom;
    	// 🧾 Panel de lista de compras (derecha)
        JPanel panelLista = new JPanel();
        panelLista.setLayout(null);
        panelLista.setBounds(marginLeft + leftPanelWidth + gapBetweenPanels, marginTop, rightPanelWidth, panelHeight-50);
        panelLista.setBackground(COLOR_PANEL);
        panelLista.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 2));
        add(panelLista);    
        // Área de texto de la lista
        this.listaTextos = new JTextArea();
        listaTextos.setBackground(Color.WHITE);
        listaTextos.setForeground(COLOR_PRIMARIO);
        listaTextos.setEditable(false);
        listaTextos.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollLista = new JScrollPane(listaTextos);
        scrollLista.setBounds(15, 20, panelLista.getWidth() - 30, panelLista.getHeight() - 150);
        scrollLista.getViewport().setBackground(Color.WHITE);
        panelLista.add(scrollLista);
        // Línea de total
        this.lblTotal = new JLabel("Total: S/ 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblTotal.setForeground(COLOR_PRIMARIO);
        lblTotal.setBounds(15, panelLista.getHeight() - 120, panelLista.getWidth() - 30, 25);
        panelLista.add(lblTotal);
        // Botones Cancelar y Aceptar
        JButton btnCancelar = crearBotonAccion("Cancelar", COLOR_SECUNDARIO);
        btnCancelar.setBounds(20, panelLista.getHeight() - 80, 280, 55);
        btnCancelar.addActionListener(e -> cancelarVenta());
        panelLista.add(btnCancelar);

        JButton btnAceptar = crearBotonAccion("Aceptar", COLOR_PRIMARIO);
        btnAceptar.setBounds(panelLista.getWidth() - 302, panelLista.getHeight() - 80, 280, 55);
        btnAceptar.addActionListener(e -> procesarVenta());
        panelLista.add(btnAceptar);
	}
	private void procesarVenta() {
	    if (controlador.getCarrito().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "El carrito está vacío", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    // Confirmar la venta
	    int confirmacion = JOptionPane.showConfirmDialog(
	        this,
	        String.format("¿Confirmar venta?\nTotal: S/ %.2f", controlador.calcularTotalCarrito()),
	        "Confirmar Venta",
	        JOptionPane.YES_NO_OPTION
	    );
	    
	    if (confirmacion == JOptionPane.YES_OPTION) {
	        if (controlador.procesarVenta()) {
	            JOptionPane.showMessageDialog(this, 
	                String.format("✅ Venta procesada exitosamente\nTotal: S/ %.2f", controlador.calcularTotalCarrito()),
	                "Venta Exitosa", 
	                JOptionPane.INFORMATION_MESSAGE
	            );
	            actualizarCarrito();
	            cargarProductos(); // Recargar para actualizar stocks
	        } else {
	            JOptionPane.showMessageDialog(this, 
	                "❌ Error al procesar la venta. Verifique el stock disponible.", 
	                "Error", 
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    }
	}
	private void actualizarBotonesProductos() {
	    cargarProductos(); // Esto recargará los productos con stock actualizado
	}
	private void cancelarVenta() {
        controlador.limpiarCarrito();
        actualizarCarrito();
        JOptionPane.showMessageDialog(this, "Venta cancelada", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
    }
	private void configurarPanelProductos() {
	    // Obtener dimensiones de la pantalla
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    
	    // CALCULAR DIMENSIONES
	    int marginLeft = 50;
	    int marginTop = 90;
	    int marginBottom = 160;
	    int gapBetweenPanels = 30;
	    
	    int panelWidth = (screenWidth - 2 * marginLeft - gapBetweenPanels);
	    int leftPanelWidth = (int)(panelWidth * 0.62);
	    int rightPanelWidth = (int)(panelWidth * 0.35);
	    int panelHeight = screenHeight - marginTop - marginBottom;
	    
	    // Panel de productos (izquierda)
	    JPanel panelProductos = new JPanel();
	    panelProductos.setLayout(null);
	    panelProductos.setBounds(marginLeft, marginTop, leftPanelWidth, panelHeight-50);
	    panelProductos.setBackground(COLOR_PANEL);
	    panelProductos.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 2));
	    add(panelProductos);

	    // Campo de búsqueda
	    JTextField txtBuscar = new JTextField();
	    txtBuscar.setBackground(Color.WHITE);
	    txtBuscar.setForeground(COLOR_PRIMARIO);
	    txtBuscar.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1));
	    txtBuscar.setBounds(20, 20, panelProductos.getWidth() - 110, 40);
	    txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
	        public void keyReleased(java.awt.event.KeyEvent evt) {
	            buscarProductos(txtBuscar.getText());
	        }
	    });
	    panelProductos.add(txtBuscar);

	    // Botón filtro
	    JButton btnFiltro = new JButton("⚙️");
	    btnFiltro.setBounds(panelProductos.getWidth() - 80, 20, 70, 40);
	    btnFiltro.setBackground(COLOR_SECUNDARIO);
	    btnFiltro.setForeground(COLOR_TEXTO_CLARO);
	    btnFiltro.setFont(new Font("Arial", Font.BOLD, 14));
	    btnFiltro.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 1));
	    panelProductos.add(btnFiltro);

	    // Crear el menú emergente de filtros
	    JPopupMenu menuFiltros = new JPopupMenu();
	    menuFiltros.setBackground(COLOR_PANEL);
	    menuFiltros.setBorder(BorderFactory.createLineBorder(COLOR_SECUNDARIO, 1));

	    // Submenú para Filtros
	    JMenu menuFiltrar = new JMenu("🔍 Filtrar por");
	    menuFiltrar.setBackground(COLOR_PANEL);
	    menuFiltrar.setForeground(COLOR_PRIMARIO);
	    
	    // Filtros por Categoría
	    JMenu subMenuCategoria = new JMenu("Categoría");
	    String[] categorias = {"Todos", "Alimentos", "Lácteos", "Limpieza", "Panadería", "Cuidado Personal", "Conservas"};
	    for (String categoria : categorias) {
	        JMenuItem itemCategoria = crearItemMenu(categoria);
	        itemCategoria.addActionListener(e -> aplicarFiltroCategoria(categoria));
	        subMenuCategoria.add(itemCategoria);
	    }
	    
	    // Filtros por Precio
	    JMenu subMenuPrecio = new JMenu("Precio");
	    String[] rangosPrecio = {"Todos", "< S/10", "> S/10"};
	    for (String rango : rangosPrecio) {
	        JMenuItem itemPrecio = crearItemMenu(rango);
	        itemPrecio.addActionListener(e -> aplicarFiltroPrecio(rango));
	        subMenuPrecio.add(itemPrecio);
	    }
	    
	    menuFiltrar.add(subMenuCategoria);
	    menuFiltrar.add(subMenuPrecio);

	    // Submenú para Ordenamiento
	    JMenu menuOrdenar = new JMenu("🔄 Ordenar por");
	    menuOrdenar.setBackground(COLOR_PANEL);
	    menuOrdenar.setForeground(COLOR_PRIMARIO);
	    
	    String[] ordenamientos = {"Nombre A-Z", "Nombre Z-A", "Precio ↑", "Precio ↓"};
	    for (String orden : ordenamientos) {
	        JMenuItem itemOrden = crearItemMenu(orden);
	        itemOrden.addActionListener(e -> aplicarOrdenamiento(orden));
	        menuOrdenar.add(itemOrden);
	    }

	    // Opción Limpiar Filtros
	    JMenuItem itemLimpiarFiltros = crearItemMenu("🗑️ Limpiar Filtros");
	    itemLimpiarFiltros.addActionListener(e -> limpiarFiltros());

	    menuFiltros.add(menuFiltrar);
	    menuFiltros.add(menuOrdenar);
	    menuFiltros.add(new JSeparator());
	    menuFiltros.add(itemLimpiarFiltros);

	    // Mostrar el menú al hacer clic en el botón
	    btnFiltro.addActionListener(e -> {
	        menuFiltros.show(btnFiltro, 0, btnFiltro.getHeight());
	    });

	 // Panel de íconos - SOLUCIÓN SIMPLE
	    this.panelIconos = new JPanel();
	    panelIconos.setLayout(new GridLayout(0, 4, 10, 10)); // 4 columnas fijas
	    panelIconos.setBackground(COLOR_PANEL);

	    // Crear un panel contenedor para el scroll
	    JPanel panelContenedor = new JPanel(new BorderLayout());
	    panelContenedor.add(panelIconos, BorderLayout.NORTH); // Importante: BorderLayout.NORTH

	    JScrollPane scroll = new JScrollPane(panelContenedor);
	    scroll.setBounds(15, 70, panelProductos.getWidth() - 30, panelProductos.getHeight() - 90);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scroll.getViewport().setBackground(COLOR_PANEL);
	    panelProductos.add(scroll);
	}
	private void configurarBarraSuperior() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        
        JLabel barraSuperior = new JLabel("SISTEMA DE VENTA - CUSQUEÑO S.A.C", SwingConstants.CENTER);
        barraSuperior.setOpaque(true);
        barraSuperior.setBackground(COLOR_PRIMARIO);
        barraSuperior.setForeground(COLOR_TEXTO_CLARO);
        barraSuperior.setFont(new Font("Arial", Font.BOLD, 16));
        barraSuperior.setBounds(50, 20, screenWidth - 150, 50);
        add(barraSuperior);
	}
	// Métodos auxiliares para crear componentes con la paleta de colores
    private JMenuItem crearItemMenu(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setBackground(COLOR_PANEL);
        item.setForeground(COLOR_PRIMARIO);
        item.setFont(new Font("Arial", Font.PLAIN, 12));
        return item;
    }

    private JButton crearBotonAccion(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorFondo);
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 1));
        return boton;
    }

    private JButton crearBotonInferior(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(160, 65));
        boton.setBackground(COLOR_SECUNDARIO);
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        return boton;
    }
 // Métodos para búsqueda, filtrado y ordenamiento
    private void buscarProductos(String texto) {
        panelIconos.removeAll();
        
        List<Producto> productos;
        if (texto == null || texto.trim().isEmpty()) {
            productos = controlador.obtenerProductos();
        } else {
            productos = controlador.buscarProductos(texto);
        }
        
        for (Producto producto : productos) {
            JButton boton = crearBotonProducto(producto);
            panelIconos.add(boton);
        }
        
        panelIconos.revalidate();
        panelIconos.repaint();
    }

    private void aplicarFiltroCategoria(String categoria) {
        controlador.aplicarFiltroCategoria(categoria);
        actualizarVistaConFiltros();
        mostrarEstadoFiltros();
    }

    private void aplicarFiltroPrecio(String rangoPrecio) {
        controlador.aplicarFiltroPrecio(rangoPrecio);
        actualizarVistaConFiltros();
        mostrarEstadoFiltros();
    }

    private void aplicarOrdenamiento(String orden) {
        controlador.aplicarOrdenamiento(orden);
        actualizarVistaConFiltros();
        mostrarEstadoFiltros();
    }

    private void limpiarFiltros() {
        controlador.limpiarFiltros();
        actualizarVistaConFiltros();
        JOptionPane.showMessageDialog(this, "Filtros limpiados", "Filtros", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarVistaConFiltros() {
        panelIconos.removeAll();
        
        for (Producto producto : controlador.obtenerProductos()) {
            JButton boton = crearBotonProducto(producto);
            panelIconos.add(boton);
        }
        
        panelIconos.revalidate();
        panelIconos.repaint();
    }

    private void mostrarEstadoFiltros() {
        String estado = String.format(
            "Filtros activos:\nCategoría: %s\nPrecio: %s\nOrden: %s",
            controlador.getFiltroCategoriaActivo(),
            controlador.getFiltroPrecioActivo(),
            controlador.getOrdenamientoActivo()
        );
        
        // Opcional: Mostrar en consola o en un tooltip
        System.out.println(estado);
    }

   
    private static Inventario crearInventarioEjemplo() {
        Inventario inventario = new Inventario();
        
        // Productos de ejemplo - esto luego vendrá de tu base de datos
        inventario.agregarProducto(new Producto("Arroz Costeño", "ARROZ001", 50, 12.50, "Alimentos"));
        inventario.agregarProducto(new Producto("Aceite Primor", "ACEITE001", 30, 18.00, "Alimentos"));
        inventario.agregarProducto(new Producto("Azúcar Rubia", "AZUCAR001", 40, 8.50, "Alimentos"));
        inventario.agregarProducto(new Producto("Leche Gloria", "LECHE001", 25, 6.50, "Lácteos"));
        inventario.agregarProducto(new Producto("Pan de Molde", "PAN001", 100, 2.50, "Panadería"));
        inventario.agregarProducto(new Producto("Jabón Bolivar", "JABON001", 60, 4.50, "Limpieza"));
        inventario.agregarProducto(new Producto("Shampoo Sedal", "SHAMPOO001", 20, 15.00, "Cuidado Personal"));
        inventario.agregarProducto(new Producto("Cereal Kellogg's", "CEREAL001", 35, 12.00, "Alimentos"));
        inventario.agregarProducto(new Producto("Atún Florida", "ATUN001", 45, 7.50, "Conservas"));
        inventario.agregarProducto(new Producto("Galletas Casino", "GALLETA001", 80, 5.50, "Alimentos"));
        
        return inventario;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Crear el modelo (Inventario y RegistroVentas)
                minimarket.negocio.Inventario inventario = crearInventarioEjemplo();
                minimarket.registro.RegistroVentas registroVentas = new minimarket.registro.RegistroVentas();
                
                // 2. Crear el controlador principal
                minimarket.controlador.ControladorPrincipal controlador = 
                    new minimarket.controlador.ControladorPrincipal(inventario, registroVentas);
                
                // 3. Crear la vista y pasarle el controlador
                MenuPrincipal ventana = new MenuPrincipal(controlador);
                ventana.setVisible(true);
                
                System.out.println("✅ Sistema iniciado correctamente con arquitectura MVC");
                System.out.println("📦 Productos cargados: " + inventario.getProductos().size());
                
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar el sistema: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar el sistema: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
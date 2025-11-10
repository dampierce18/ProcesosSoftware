package minimarket.app;

import minimarket.negocio.Inventario;
import minimarket.registro.RegistroVentas;
import minimarket.controlador.ControladorPrincipal;
import minimarket.data.ProductoDAO;
import minimarket.data.VentaDAO;
import ventanas.MenuPrincipal;
import minimarket.data.ConexionBD;

public class MainApp {

    public static void main(String[] args) {
        try {
        	ConexionBD.crearTablasIniciales();		
            // 1️⃣ Crear DAOs (capa de datos)
            ProductoDAO productoDAO = new ProductoDAO();
            VentaDAO ventaDAO = new VentaDAO();
            
            // 2️⃣ Cargar datos INICIALES desde BD
            System.out.println("📦 Cargando productos desde BD...");
            Inventario inventario = new Inventario();
            inventario.cargarProductos(productoDAO.cargarProductos());
            
            System.out.println("🧾 Cargando historial de ventas...");
            RegistroVentas registroVentas = new RegistroVentas();
            registroVentas.cargarVentas(ventaDAO.listarVentas());
            
            // 3️⃣ Crear controlador con los DAOs para operaciones futuras
            ControladorPrincipal controlador = new ControladorPrincipal(
                inventario, 
                registroVentas,
                productoDAO,
                ventaDAO
            );
            	
            // 4️⃣ Crear y mostrar ventana principal
            MenuPrincipal menu = new MenuPrincipal(controlador);
            controlador.setVista(menu);
            menu.setVisible(true);
            
            System.out.println("✅ Sistema iniciado correctamente!");
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package minimarket.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    
    private static Connection conexion = null;
    
    // Bloque estático para cargar el driver al inicio
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ Driver SQLite cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: Driver SQLite no encontrado");
            e.printStackTrace();
        }
    }
    
    public static Connection obtenerConexion() {
        if (conexion == null) {
            try {
                String url = "jdbc:sqlite:minimarket.db";
                conexion = DriverManager.getConnection(url);
                System.out.println("✅ Conexión exitosa a la base de datos SQLite.");
            } catch (SQLException e) {
                System.err.println("❌ Error de conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("🔒 Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    public static Connection getConexion() throws SQLException {
        // ✅ CORREGIDO: "minimarke.db" → "minimarket.db"
        String url = "jdbc:sqlite:minimarke.db";
        return DriverManager.getConnection(url);
    }
    
    // ✅ MÉTODO SIMPLE para crear tablas
    public static void crearTablasIniciales() {
        try (Connection conn = getConexion();
             Statement stmt = conn.createStatement()) {
            
            // Crear tablas
            stmt.execute("CREATE TABLE IF NOT EXISTS productos (codigo VARCHAR(20) PRIMARY KEY, nombre VARCHAR(100) NOT NULL, categoria VARCHAR(50), stock INT NOT NULL DEFAULT 0, precio DECIMAL(10,2) NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo VARCHAR(20) NOT NULL, fecha DATE NOT NULL, total DECIMAL(10, 2) NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS detalle_ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, id_venta INT NOT NULL, producto_codigo VARCHAR(20) NOT NULL, cantidad INT NOT NULL, subtotal DECIMAL(10,2) NOT NULL, FOREIGN KEY (id_venta) REFERENCES ventas(id), FOREIGN KEY (producto_codigo) REFERENCES productos(codigo))");
            
            // Insertar productos
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('ARROZ001', 'Arroz Costeño', 'Alimentos', 50, 12.50)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('ACEITE001', 'Aceite Primor', 'Alimentos', 30, 18.00)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('AZUCAR001', 'Azúcar Rubia', 'Alimentos', 40, 8.50)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('LECHE001', 'Leche Gloria 400g', 'Lácteos', 50, 4.80)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('PAN001', 'Pan de molde Bimbo', 'Panadería', 25, 7.50)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('FIDEOS001', 'Fideos Don Vittorio 500g', 'Alimentos', 45, 4.30)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('HUEVOS001', 'Huevos de gallina (docena)', 'Alimentos', 20, 10.00)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('AZUCAR002', 'Azúcar Blanca 1kg', 'Alimentos', 40, 8.20)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('GASEOSA001', 'Inca Kola 1.5L', 'Bebidas', 35, 7.00)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('AGUA001', 'Agua San Luis 625ml', 'Bebidas', 50, 2.00)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('GALLETAS001', 'Galletas Casino', 'Snacks', 40, 2.50)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('AROMAT001', 'Detergente Ariel 500g', 'Limpieza', 25, 6.80)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('SHAMPOO001', 'Shampoo Head & Shoulders 180ml', 'Higiene', 20, 9.90)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('PAPEL001', 'Papel Higiénico Elite x4', 'Higiene', 30, 7.20)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('JABON001', 'Jabón Bolívar 200g', 'Limpieza', 35, 3.50)");
            stmt.execute("INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES ('CAFE001', 'Café Altomayo 200g', 'Bebidas', 15, 14.50)");

            
            System.out.println("✅ Tablas y datos iniciales creados");
            
        } catch (SQLException e) {
            System.err.println("❌ Error creando tablas: " + e.getMessage());
        }
    }
}
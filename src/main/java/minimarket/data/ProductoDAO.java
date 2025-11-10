package minimarket.data;

import minimarket.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

	public List<Producto> cargarProductos() {
	    List<Producto> lista = new ArrayList<>();
	    
	    // ✅ CORREGIR: agregar categoria al SELECT
	    String sql = "SELECT codigo, nombre, stock, precio, categoria FROM productos"; // ← Agregado
	    
	    try (Connection conn = ConexionBD.getConexion();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        while (rs.next()) {
	            Producto p = new Producto(
	                rs.getString("nombre"),
	                rs.getString("codigo"),
	                rs.getInt("stock"),
	                rs.getDouble("precio"),
	                rs.getString("categoria")  // ← Ahora sí existe
	            );
	            lista.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lista;
	}
	public boolean actualizarStock(String codigo, int cantidadVendida) {
	    String sql = "UPDATE productos SET stock = stock - ? WHERE codigo = ? AND stock >= ?";
	    
	    try (Connection conn = ConexionBD.getConexion();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, cantidadVendida);
	        ps.setString(2, codigo);
	        ps.setInt(3, cantidadVendida); // Validación de stock suficiente
	        
	        int filasAfectadas = ps.executeUpdate();
	        return filasAfectadas > 0;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}

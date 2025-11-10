package minimarket.data;

import minimarket.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public void guardarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO ventas (codigo, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, producto_codigo, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {

            conn.setAutoCommit(false); // Transacción

            // Insertamos la venta principal
            psVenta.setString(1, venta.getCodigo());
            psVenta.setDate(2, Date.valueOf(venta.getFecha()));
            psVenta.setDouble(3, venta.getMontoTotal());
            psVenta.executeUpdate();

            // Recuperamos el id autogenerado
            ResultSet rs = psVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rs.next()) idVenta = rs.getInt(1);

            // Insertamos cada detalle
            for (DetalleVenta d : venta.getDetalles()) {
                psDetalle.setInt(1, idVenta);
                psDetalle.setString(2, d.getProducto().getCodigo());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getSubtotal());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            conn.commit();
            System.out.println("✅ Venta registrada correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al registrar la venta: " + e.getMessage());
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();

        String sql = """
            SELECT v.id AS id_venta, v.codigo AS codigo_venta, v.fecha, v.total,
                   p.codigo AS producto_codigo, p.nombre AS producto_nombre, 
                   p.precio AS producto_precio, p.categoria AS producto_categoria,
                   dv.cantidad, dv.subtotal
            FROM ventas v
            JOIN detalle_ventas dv ON v.id = dv.id_venta
            JOIN productos p ON dv.producto_codigo = p.codigo
            ORDER BY v.id;
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Venta ventaActual = null;
            int idVentaActual = -1;

            while (rs.next()) {
                int idVenta = rs.getInt("id_venta");

                // Si cambiamos de venta, creamos una nueva
                if (idVenta != idVentaActual) {
                    ventaActual = new Venta(
                        rs.getString("codigo_venta"),
                        rs.getDate("fecha").toLocalDate()
                    );
                    lista.add(ventaActual);
                    idVentaActual = idVenta;
                }

                // Crear producto
                Producto prod = new Producto(
                    rs.getString("producto_nombre"),
                    rs.getString("producto_codigo"),
                    0, // no necesitamos stock aquí
                    rs.getDouble("producto_precio"),
                    rs.getString("producto_categoria")
                );

                // Crear detalle
                DetalleVenta det = new DetalleVenta(prod, rs.getInt("cantidad"));
                ventaActual.agregarDetalle(det);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error al listar ventas: " + e.getMessage());
        }

        return lista;
    }

}

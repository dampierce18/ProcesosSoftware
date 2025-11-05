package minimarket.negocio;

import minimarket.modelo.Producto;
import minimarket.modelo.Venta;
import minimarket.registro.RegistroVentas;
import minimarket.modelo.DetalleVenta;

import java.util.List;

public class GestorVentas {

    private RegistroVentas registroVentas;

    public GestorVentas(RegistroVentas registroVentas) {
        this.registroVentas = registroVentas;
    }

    public boolean validarStock(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) return false;
        return producto.getStock() >= cantidad;
    }

    public boolean agregarDetalle(Venta venta, Producto producto, int cantidad) {
        if (venta == null || producto == null) return false;
        if (!validarStock(producto, cantidad)) return false;

        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        venta.agregarDetalle(detalle);
        return true;
    }

    public boolean procesarVenta(Venta venta) {
        if (venta == null) return false;

        List<DetalleVenta> detalles = venta.getDetalles();
        if (detalles == null || detalles.isEmpty()) return false;

        for (DetalleVenta d : detalles) {
            if (!validarStock(d.getProducto(), d.getCantidad())) {
                System.out.println("❌ No hay stock suficiente para " + d.getProducto().getNombre());
                return false;
            }
        }

        for (DetalleVenta d : detalles) {
            Producto p = d.getProducto();
            p.reducirStock(d.getCantidad());
        }

        double total = venta.calcularMontoTotal();
        registroVentas.registrarVenta(venta);

        System.out.println("✅ Venta procesada correctamente. Total: S/ " + total);
        return true;
    }

    public void anularVenta(Venta venta) {
        if (venta == null) return;
        for (DetalleVenta d : venta.getDetalles()) {
            Producto p = d.getProducto();
            p.aumentarStock(d.getCantidad());
        }
        registroVentas.eliminarVenta(venta);
        System.out.println("⚠️ Venta anulada y stock restaurado.");
    }

    public List<Venta> obtenerHistorialVentas() {
        return registroVentas.obtenerVentas();
    }
}


package minimarket.negocio;

import minimarket.modelo.*;
import minimarket.registro.RegistroPedidos;
import java.util.List;

public class GestorPedidos {
    private RegistroPedidos registroPedidos;

    // 🔹 Inyección de dependencia por constructor
    public GestorPedidos(RegistroPedidos registroPedidos) {
        this.registroPedidos = registroPedidos;
    }

    public boolean agregarDetalle(Pedido pedido, Producto producto, int cantidad) {
        if (pedido == null || producto == null || cantidad <= 0) return false;

        DetallePedido detalle = new DetallePedido(producto, cantidad);
        pedido.agregarDetalle(detalle);
        return true;
    }

    public boolean procesarPedido(Pedido pedido) {
        if (pedido == null) return false;

        List<DetallePedido> detalles = pedido.getDetalles();
        if (detalles == null || detalles.isEmpty()) return false;

        double total = pedido.calcularMontoTotal();
        registroPedidos.registrarPedido(pedido);

        System.out.println("✅ Pedido registrado correctamente. Total: S/ " + total);
        return true;
    }

    public void cancelarPedido(Pedido pedido) {
        if (pedido == null) return;

        registroPedidos.eliminarPedido(pedido);
        System.out.println("⚠️ Pedido cancelado correctamente.");
    }

    public List<Pedido> obtenerHistorialPedidos() {
        return registroPedidos.obtenerPedidos();
    }
}

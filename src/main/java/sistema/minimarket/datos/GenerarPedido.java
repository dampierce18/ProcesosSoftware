package sistema.minimarket.datos;

import java.util.ArrayList;
import java.util.List;
import sistema.minimarket.modelo.Pedido;
import sistema.minimarket.modelo.Producto;

public class GenerarPedido {

    public static List<Pedido> generarPedidosPrueba() {
        List<Pedido> pedidos = new ArrayList<>();

        // Productos de ejemplo
        Producto arroz = new Producto("Arroz", "P001", 20, 3.5, "Abarrotes");
        Producto gaseosa = new Producto("Gaseosa", "P002", 15, 6.0, "Bebidas");
        Producto detergente = new Producto("Detergente", "P003", 10, 7.5, "Limpieza");

        // Pedidos (solicitar más unidades)
        pedidos.add(new Pedido(arroz, 50));
        pedidos.add(new Pedido(gaseosa, 30));
        pedidos.add(new Pedido(detergente, 20));

        return pedidos;
    }

    // Método para mostrar los pedidos en consola
    public static void mostrarPedidos(List<Pedido> pedidos) {
        System.out.println("=== LISTA DE PEDIDOS GENERADOS ===");
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
    }
}
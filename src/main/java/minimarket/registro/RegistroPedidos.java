package minimarket.registro;

import minimarket.modelo.Pedido;
import java.util.ArrayList;
import java.util.List;

public class RegistroPedidos {
    private List<Pedido> listaPedidos = new ArrayList<>();

    public void registrarPedido(Pedido pedido) {
        listaPedidos.add(pedido);
    }

    public void eliminarPedido(Pedido pedido) {
        listaPedidos.remove(pedido);
    }

    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(listaPedidos); // devuelve copia para evitar modificaciones externas
    }

    public void mostrarHistorial() {
        if (listaPedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE PEDIDOS ===");
        for (Pedido p : listaPedidos) {
            System.out.println(p);
        }
    }
}

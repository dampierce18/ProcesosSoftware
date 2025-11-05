package minimarket.registro;

import minimarket.modelo.Venta;
import java.util.ArrayList;
import java.util.List;

public class RegistroVentas {
    private List<Venta> listaVentas = new ArrayList<>();

    public void registrarVenta(Venta venta) {
        listaVentas.add(venta);
    }

    public void eliminarVenta(Venta venta) {
        listaVentas.remove(venta);
    }

    public List<Venta> obtenerVentas() {
        return new ArrayList<>(listaVentas); // Retorna copia para evitar modificaciones externas
    }

    public void mostrarHistorial() {
        if (listaVentas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE VENTAS ===");
        for (Venta v : listaVentas) {
            System.out.println(v);
        }
    }
}

package minimarket.negocio;

import minimarket.modelo.*;
import minimarket.registro.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestorConsultas {
    private RegistroVentas registro;

    public GestorConsultas(RegistroVentas registro) {
        this.registro = registro;
    }

    // 🔹 Obtener todas las ventas de un rango de fechas
    public List<Venta> obtenerVentasEntre(LocalDate inicio, LocalDate fin) {
        return registro.obtenerVentas().stream()
                .filter(v -> !v.getFecha().isBefore(inicio) && !v.getFecha().isAfter(fin))
                .collect(Collectors.toList());
    }

    // 🔹 Calcular monto total
    public double calcularMontoTotal(LocalDate inicio, LocalDate fin) {
        return obtenerVentasEntre(inicio, fin).stream()
                .mapToDouble(Venta::getMontoTotal)
                .sum();
    }

    // 🔹 Contar cantidad de ventas
    public long contarVentas(LocalDate inicio, LocalDate fin) {
        return obtenerVentasEntre(inicio, fin).size();
    }

    // 🔹 Producto más vendido en rango
    public Producto obtenerProductoMasVendido(LocalDate inicio, LocalDate fin) {
        Map<Producto, Integer> conteo = new HashMap<>();

        for (Venta v : obtenerVentasEntre(inicio, fin)) {
            for (DetalleVenta d : v.getDetalles()) {
                conteo.merge(d.getProducto(), d.getCantidad(), Integer::sum);
            }
        }

        return conteo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

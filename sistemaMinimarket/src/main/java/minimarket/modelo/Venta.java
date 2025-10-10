package minimarket.modelo;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String codigo;
    private String fecha;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

    public Venta(String codigo, String fecha) {
        this.codigo = codigo;
        this.fecha = fecha;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    // Calculamos el total en tiempo real para evitar inconsistencias
    public double calcularMontoTotal() {
        double total = 0;
        for (DetalleVenta d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public double getMontoTotal() {
        return calcularMontoTotal();
    }

    public String getCodigo() { return codigo; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta ").append(codigo)
          .append(" | Fecha: ").append(fecha)
          .append(" | Monto total: S/ ").append(getMontoTotal())
          .append("\nDetalles:\n");
        for (DetalleVenta d : detalles) {
            sb.append("  - ").append(d.toString()).append("\n");
        }
        return sb.toString();
    }
}

package minimarket.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Pedido {
    private String codigo;
    private LocalDate fecha;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {}

    public Pedido(String codigo, LocalDate fecha) {
        this.codigo = codigo;
        this.fecha = fecha;
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public double calcularMontoTotal() {
        double total = 0;
        for (DetallePedido d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public double getMontoTotal() {
        return calcularMontoTotal();
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido ").append(codigo)
          .append(" | Fecha: ").append(fecha)
          .append(" | Monto total: S/ ").append(getMontoTotal())
          .append("\nDetalles:\n");
        for (DetallePedido d : detalles) {
            sb.append("  - ").append(d.toString()).append("\n");
        }
        return sb.toString();
    }
}

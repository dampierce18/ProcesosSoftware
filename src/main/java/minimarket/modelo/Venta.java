package minimarket.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Venta {
    private String codigo;
    private LocalDate fecha;
    private double montoTotal;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {}

    public Venta(String codigo, LocalDate fecha) {
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
        montoTotal = 0;
        for (DetalleVenta d : detalles) {
            montoTotal += d.getSubtotal();
        }
        return montoTotal;
    }

    public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public void setDetalles(List<DetalleVenta> detalles) {
		this.detalles = detalles;
	}

	public double getMontoTotal() {
        return calcularMontoTotal();
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFecha() { return fecha; }

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

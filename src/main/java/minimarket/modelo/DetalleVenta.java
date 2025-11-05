package minimarket.modelo;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; recalcularSubtotal(); }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; recalcularSubtotal(); }

    public double getSubtotal() { return subtotal; }

    private void recalcularSubtotal() {
        if (producto != null) this.subtotal = producto.getPrecio() * cantidad;
        else this.subtotal = 0;
    }

    @Override
    public String toString() {
        return producto.getNombre() + " (x" + cantidad + ") - S/ " + subtotal;
    }
}

package sistema.minimarket.modelo;


public class Pedido {
    private Producto producto;
    private int cantidad;

    public Pedido() {}

    public Pedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Pedido -> Producto: " + producto.getNombre() +
               " | Cantidad solicitada: " + cantidad;
    }
}
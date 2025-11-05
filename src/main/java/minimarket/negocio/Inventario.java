package minimarket.negocio;

import java.util.ArrayList;
import java.util.List;
import minimarket.modelo.Producto;

public class Inventario {
    private List<Producto> productos;

    public Inventario() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public void mostrarProductos() {
        System.out.println("\n--- Productos disponibles ---");
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            System.out.printf("[%d] %s - S/%.2f - Stock: %d\n",
                i + 1, p.getNombre(), p.getPrecio(), p.getStock());
        }
    }
}

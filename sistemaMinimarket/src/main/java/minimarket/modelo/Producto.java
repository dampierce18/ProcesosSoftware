package minimarket.modelo;

public class Producto {
    private String nombre;
    private String codigo;
    private int stock;
    private double precio;
    private String categoria;  // Nuevo atributo

    public Producto() {}

    public Producto(String nombre, String codigo, int stock, double precio, String categoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.stock = stock;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public void aumentarStock(int cantidad) {
        if (cantidad > 0) this.stock += cantidad;
    }

    public void reducirStock(int cantidad) {
        if (cantidad > 0 && cantidad <= this.stock) this.stock -= cantidad;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | " +
               "Nombre: " + nombre + " | " +
               "Categoría: " + categoria + " | " +
               "Stock: " + stock + " | " +
               "Precio: S/ " + precio;
    }
}


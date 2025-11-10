/*package minimarket.controlador;

import minimarket.modelo.Producto;
import minimarket.modelo.DetalleVenta;
import minimarket.modelo.Venta;
import minimarket.negocio.Inventario;
import minimarket.negocio.GestorVentas;
import minimarket.registro.RegistroVentas;
import ventanas.MenuPrincipal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorPrincipal {
    private Inventario inventario;
    private GestorVentas gestorVentas;
    private MenuPrincipal vista;
    private List<DetalleVenta> carrito;
    
    // Estados para filtros y ordenamiento
    private String filtroCategoriaActivo = "Todos";
    private String filtroPrecioActivo = "Todos";
    private String ordenamientoActivo = "Nombre A-Z";
    
    public ControladorPrincipal(Inventario inventario, RegistroVentas registroVentas) {
        this.inventario = inventario;
        this.gestorVentas = new GestorVentas(registroVentas);
        this.carrito = new ArrayList<>();
    }
    
    public void setVista(MenuPrincipal vista) {
        this.vista = vista;
    }
    
    // Métodos para la vista
    public List<Producto> obtenerProductos() {
        return aplicarFiltrosYOrdenamiento(inventario.getProductos()); // ✅ CORRECTO
    }
    
    public boolean validarStock(Producto producto, int cantidad) {
        return gestorVentas.validarStock(producto, cantidad);
    }
    
    public void agregarAlCarrito(Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        carrito.add(detalle);
    }
    
    public List<DetalleVenta> getCarrito() {
        return new ArrayList<>(carrito);
    }
    
    public void limpiarCarrito() {
        carrito.clear();
    }
    
    public double calcularTotalCarrito() {
        double total = 0;
        for (DetalleVenta detalle : carrito) {
            total += detalle.getSubtotal();
        }
        return total;
    }
    
    public boolean procesarVenta() {
        if (carrito.isEmpty()) {
            return false;
        }
        
        try {
            // 1. Crear una venta
            String codigoVenta = "VTA-" + UUID.randomUUID().toString().substring(0, 8);
            Venta venta = new Venta(codigoVenta, LocalDate.now());
            
            // 2. Agregar todos los detalles del carrito a la venta
            for (DetalleVenta detalle : carrito) {
                venta.agregarDetalle(detalle);
            }
            
            // 3. Usar el GestorVentas para procesar la venta (esto actualizará el stock)
            boolean exito = gestorVentas.procesarVenta(venta);
            
            if (exito) {
                // 4. Limpiar carrito solo si la venta fue exitosa
                limpiarCarrito();
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error al procesar venta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // Método para obtener productos con stock actualizado
    public List<Producto> obtenerProductosActualizados() {
        return inventario.getProductos();
    }
    
    // Búsqueda independiente de filtros
    public List<Producto> buscarProductos(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return inventario.getProductos(); // ✅ Búsqueda independiente - muestra TODOS
        }
        
        String textoLower = texto.toLowerCase();
        return inventario.getProductos().stream()
            .filter(p -> p.getNombre().toLowerCase().contains(textoLower))
            .collect(Collectors.toList());
    }
    // Filtros
    public void aplicarFiltroCategoria(String categoria) {
        this.filtroCategoriaActivo = categoria;
    }
    
    public void aplicarFiltroPrecio(String rangoPrecio) {
        this.filtroPrecioActivo = rangoPrecio;
    }
    
    public void aplicarOrdenamiento(String orden) {
        this.ordenamientoActivo = orden;
    }
    
    public void limpiarFiltros() {
        this.filtroCategoriaActivo = "Todos";
        this.filtroPrecioActivo = "Todos";
        this.ordenamientoActivo = "Nombre A-Z";
    }
    
    public String getFiltroCategoriaActivo() {
        return filtroCategoriaActivo;
    }
    
    public String getFiltroPrecioActivo() {
        return filtroPrecioActivo;
    }
    
    public String getOrdenamientoActivo() {
        return ordenamientoActivo;
    }	
    
	private List<Producto> aplicarFiltrosYOrdenamiento(List<Producto> productos) {
        List<Producto> resultado = new ArrayList<>(productos);
        
        // Aplicar filtro de categoría
        if (!filtroCategoriaActivo.equals("Todos")) {
            resultado = resultado.stream()
                .filter(p -> p.getCategoria().equals(filtroCategoriaActivo))
                .collect(Collectors.toList());
        }
        
        // Aplicar filtro de precio
        switch (filtroPrecioActivo) {
            case "< S/10":
                resultado = resultado.stream()
                    .filter(p -> p.getPrecio() < 10)
                    .collect(Collectors.toList());
                break;
            case "> S/10":
                resultado = resultado.stream()
                    .filter(p -> p.getPrecio() > 10)
                    .collect(Collectors.toList());
                break;
        }
        
        // Aplicar ordenamiento
        switch (ordenamientoActivo) {
            case "Nombre A-Z":
                resultado.sort(Comparator.comparing(Producto::getNombre));
                break;
            case "Nombre Z-A":
                resultado.sort(Comparator.comparing(Producto::getNombre).reversed());
                break;
            case "Precio ↑":
                resultado.sort(Comparator.comparing(Producto::getPrecio));
                break;
            case "Precio ↓":
                resultado.sort(Comparator.comparing(Producto::getPrecio).reversed());
                break;
        }
        
        return resultado;
    }
    // En ControladorPrincipal - método para debug
    public void imprimirEstadoFiltros() {
        System.out.println("=== ESTADO FILTROS ===");
        System.out.println("Categoría: " + filtroCategoriaActivo);
        System.out.println("Precio: " + filtroPrecioActivo);
        System.out.println("Ordenamiento: " + ordenamientoActivo);
        
        List<Producto> productos = aplicarFiltrosYOrdenamiento(inventario.getProductos());
        System.out.println("Productos mostrados: " + productos.size());
        for (Producto p : productos) {
            System.out.println(" - " + p.getNombre() + " | " + p.getCategoria() + " | S/" + p.getPrecio());
        }
        System.out.println("======================");
    }
    
}*/
package minimarket.controlador;

import minimarket.modelo.Producto;
import minimarket.modelo.DetalleVenta;
import minimarket.modelo.Venta;
import minimarket.negocio.Inventario;
import minimarket.negocio.GestorVentas;
import minimarket.registro.RegistroVentas;
import minimarket.data.ProductoDAO;
import minimarket.data.VentaDAO;
import ventanas.MenuPrincipal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

public class ControladorPrincipal {
    private Inventario inventario;
    private GestorVentas gestorVentas;
    private MenuPrincipal vista;
    private List<DetalleVenta> carrito;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    
    // Estados para filtros y ordenamiento
    private String filtroCategoriaActivo = "Todos";
    private String filtroPrecioActivo = "Todos";
    private String ordenamientoActivo = "Nombre A-Z";
    
    public ControladorPrincipal(Inventario inventario, RegistroVentas registroVentas,
            ProductoDAO productoDAO, VentaDAO ventaDAO) {
    	this.inventario = inventario;
    	this.gestorVentas = new GestorVentas(registroVentas);
    	this.productoDAO = productoDAO;
    	this.ventaDAO = ventaDAO;
    	this.carrito = new ArrayList<>();
}
    
    public void setVista(MenuPrincipal vista) {
        this.vista = vista;
    }
    
    // Métodos para la vista
    public List<Producto> obtenerProductos() {
        return aplicarFiltrosYOrdenamiento(inventario.getProductos()); // ✅ CORRECTO
    }
    
    public boolean validarStock(Producto producto, int cantidad) {
        return gestorVentas.validarStock(producto, cantidad);
    }
    
    public void agregarAlCarrito(Producto producto, int cantidad) {
        // Buscar si el producto ya está en el carrito
        for (DetalleVenta detalle : carrito) {
            if (detalle.getProducto().getCodigo().equals(producto.getCodigo())) {
                // Si ya existe, aumentar la cantidad
                detalle.setCantidad(detalle.getCantidad() + cantidad);
                return;
            }
        }
        
        // Si no existe, agregar nuevo detalle
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        carrito.add(detalle);
    }
    
    public void removerDelCarrito(Producto producto, int cantidad) {
        // Buscar el producto en el carrito
        for (int i = 0; i < carrito.size(); i++) {
            DetalleVenta detalle = carrito.get(i);
            if (detalle.getProducto().getCodigo().equals(producto.getCodigo())) {
                if (detalle.getCantidad() > cantidad) {
                    // Reducir la cantidad
                    detalle.setCantidad(detalle.getCantidad() - cantidad);
                } else {
                    // Eliminar completamente si la cantidad a remover es mayor o igual
                    carrito.remove(i);
                }
                return;
            }
        }
    }
    
    /**
     * Remueve completamente un producto del carrito
     */
    public void removerProductoCompletamente(Producto producto) {
        carrito.removeIf(detalle -> detalle.getProducto().getCodigo().equals(producto.getCodigo()));
    }

    /**
     * Obtiene la cantidad de un producto específico en el carrito
     */
    public int getCantidadEnCarrito(Producto producto) {
        for (DetalleVenta detalle : carrito) {
            if (detalle.getProducto().getCodigo().equals(producto.getCodigo())) {
                return detalle.getCantidad();
            }
        }
        return 0;
    }
    
    public List<DetalleVenta> getCarrito() {
        return new ArrayList<>(carrito);
    }
    
    public void limpiarCarrito() {
        carrito.clear();
    }
    
    public double calcularTotalCarrito() {
        double total = 0;
        for (DetalleVenta detalle : carrito) {
            total += detalle.getSubtotal();
        }
        return total;
    }
    
    public boolean procesarVenta() {
        if (carrito.isEmpty()) {
            return false;
        }
        
        try {
            // 1. Crear una venta
            String codigoVenta = "VTA-" + UUID.randomUUID().toString().substring(0, 8);
            Venta venta = new Venta(codigoVenta, LocalDate.now());
            
            // 2. Agregar todos los detalles del carrito a la venta
            for (DetalleVenta detalle : carrito) {
                venta.agregarDetalle(detalle);
            }
            
            // 3. 🆕 PERSISTIR EN BASE DE DATOS
            System.out.println("💾 Persistiendo venta en BD...");
            ventaDAO.guardarVenta(venta);
            
            // 4. 🆕 ACTUALIZAR STOCKS EN BD para cada producto
            for (DetalleVenta detalle : carrito) {
                String codigoProducto = detalle.getProducto().getCodigo();
                int cantidadVendida = detalle.getCantidad();
                
                boolean stockActualizado = productoDAO.actualizarStock(codigoProducto, cantidadVendida);
                if (!stockActualizado) {
                    System.err.println("❌ No se pudo actualizar stock para: " + codigoProducto);
                    return false;
                }
            }
            
            // 5. Usar el GestorVentas para procesar la venta (actualiza modelos locales)
            boolean exito = gestorVentas.procesarVenta(venta);
            
            if (exito) {
                // 6. 🆕 SINCRONIZAR: Recargar productos desde BD para asegurar consistencia
                System.out.println("🔄 Sincronizando productos desde BD...");
                List<Producto> productosActualizados = productoDAO.cargarProductos();
                inventario.cargarProductos(productosActualizados);
                
                // 7. Limpiar carrito solo si todo fue exitoso
                limpiarCarrito();
                
                // 8. 🆕 Notificar a la vista para que actualice la interfaz
                if (vista != null) {
                    vista.actualizarVistaProductos();
                }
                
                System.out.println("✅ Venta procesada y persistida correctamente");
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar venta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // 🆕 MÉTODO para sincronización manual
    public void sincronizarProductosDesdeBD() {
        try {
            System.out.println("🔄 Sincronizando productos desde BD...");
            List<Producto> productosActualizados = productoDAO.cargarProductos();
            inventario.cargarProductos(productosActualizados);
            
            if (vista != null) {
                vista.actualizarVistaProductos();
            }
            System.out.println("✅ Productos sincronizados correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al sincronizar productos: " + e.getMessage());
        }
    }
    // Método para obtener productos con stock actualizado
    public List<Producto> obtenerProductosActualizados() {
        return inventario.getProductos();
    }
    
    // Búsqueda independiente de filtros
    public List<Producto> buscarProductos(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return inventario.getProductos(); // ✅ Búsqueda independiente - muestra TODOS
        }
        
        String textoLower = texto.toLowerCase();
        return inventario.getProductos().stream()
            .filter(p -> p.getNombre().toLowerCase().contains(textoLower))
            .collect(Collectors.toList());
    }
    // Filtros
    public void aplicarFiltroCategoria(String categoria) {
        this.filtroCategoriaActivo = categoria;
    }
    
    public void aplicarFiltroPrecio(String rangoPrecio) {
        this.filtroPrecioActivo = rangoPrecio;
    }
    
    public void aplicarOrdenamiento(String orden) {
        this.ordenamientoActivo = orden;
    }
    
    public void limpiarFiltros() {
        this.filtroCategoriaActivo = "Todos";
        this.filtroPrecioActivo = "Todos";
        this.ordenamientoActivo = "Nombre A-Z";
    }
    
    public String getFiltroCategoriaActivo() {
        return filtroCategoriaActivo;
    }
    
    public String getFiltroPrecioActivo() {
        return filtroPrecioActivo;
    }
    
    public String getOrdenamientoActivo() {
        return ordenamientoActivo;
    }	
    
	private List<Producto> aplicarFiltrosYOrdenamiento(List<Producto> productos) {
        List<Producto> resultado = new ArrayList<>(productos);
        
        // Aplicar filtro de categoría
        if (!filtroCategoriaActivo.equals("Todos")) {
            resultado = resultado.stream()
                .filter(p -> p.getCategoria().equals(filtroCategoriaActivo))
                .collect(Collectors.toList());
        }
        
        // Aplicar filtro de precio
        switch (filtroPrecioActivo) {
            case "< S/10":
                resultado = resultado.stream()
                    .filter(p -> p.getPrecio() < 10)
                    .collect(Collectors.toList());
                break;
            case "> S/10":
                resultado = resultado.stream()
                    .filter(p -> p.getPrecio() > 10)
                    .collect(Collectors.toList());
                break;
        }
        
        // Aplicar ordenamiento
        switch (ordenamientoActivo) {
            case "Nombre A-Z":
                resultado.sort(Comparator.comparing(Producto::getNombre));
                break;
            case "Nombre Z-A":
                resultado.sort(Comparator.comparing(Producto::getNombre).reversed());
                break;
            case "Precio ↑":
                resultado.sort(Comparator.comparing(Producto::getPrecio));
                break;
            case "Precio ↓":
                resultado.sort(Comparator.comparing(Producto::getPrecio).reversed());
                break;
        }
        
        return resultado;
    }
    // En ControladorPrincipal - método para debug
    public void imprimirEstadoFiltros() {
        System.out.println("=== ESTADO FILTROS ===");
        System.out.println("Categoría: " + filtroCategoriaActivo);
        System.out.println("Precio: " + filtroPrecioActivo);
        System.out.println("Ordenamiento: " + ordenamientoActivo);
        
        List<Producto> productos = aplicarFiltrosYOrdenamiento(inventario.getProductos());
        System.out.println("Productos mostrados: " + productos.size());
        for (Producto p : productos) {
            System.out.println(" - " + p.getNombre() + " | " + p.getCategoria() + " | S/" + p.getPrecio());
        }
        System.out.println("======================");
    }
    
}

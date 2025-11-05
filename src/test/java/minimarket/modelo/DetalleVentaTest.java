package minimarket.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DetalleVentaTest {

    @Test
    void testConstructorConParametros() {
        Producto p = new Producto("Arroz", "P001", 50, 2.5, "Granos");
        DetalleVenta detalle = new DetalleVenta(p, 4);

        assertEquals(p, detalle.getProducto());
        assertEquals(4, detalle.getCantidad());
        assertEquals(10.0, detalle.getSubtotal());
    }

    @Test
    void testSetProducto() {
        Producto p1 = new Producto("Azúcar", "P002", 30, 3.0, "Granos");
        Producto p2 = new Producto("Aceite", "P003", 20, 5.0, "Abarrotes");

        DetalleVenta detalle = new DetalleVenta(p1, 2);
        detalle.setProducto(p2); // cambia producto → recalcula subtotal

        assertEquals(p2, detalle.getProducto());
        assertEquals(10.0, detalle.getSubtotal());
    }

    @Test
    void testSetCantidad() {
        Producto p = new Producto("Fideos", "P004", 40, 4.0, "Pastas");
        DetalleVenta detalle = new DetalleVenta(p, 1);

        detalle.setCantidad(3); // recalcula subtotal
        assertEquals(3, detalle.getCantidad());
        assertEquals(12.0, detalle.getSubtotal());
    }

    @Test
    void testSetProductoNull() {
        Producto p = new Producto("Leche", "P005", 10, 3.5, "Lácteos");
        DetalleVenta detalle = new DetalleVenta(p, 2);

        detalle.setProducto(null);
        assertNull(detalle.getProducto());
        assertEquals(0.0, detalle.getSubtotal());
    }

    @Test
    void testRecalcularSubtotalIndirectamente() {
        Producto p = new Producto("Gaseosa", "P006", 25, 2.0, "Bebidas");
        DetalleVenta detalle = new DetalleVenta(p, 5);
        assertEquals(10.0, detalle.getSubtotal());

        // Cambiamos cantidad para forzar recalculo
        detalle.setCantidad(10);
        assertEquals(20.0, detalle.getSubtotal());
    }

    @Test
    void testToString() {
        Producto p = new Producto("Pan", "P007", 15, 1.5, "Panadería");
        DetalleVenta detalle = new DetalleVenta(p, 3);

        String texto = detalle.toString();
        assertTrue(texto.contains("Pan"));
        assertTrue(texto.contains("3"));
        assertTrue(texto.contains("S/ 4.5"));
    }
}

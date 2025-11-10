package minimarket.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void testVenta() {
        Venta venta = new Venta();
        assertNotNull(venta);
        assertEquals(0, venta.getMontoTotal());
        assertNotNull(venta.getDetalles());
    }

    @Test
    void testVentaStringString() {
        Venta venta = new Venta("V001", LocalDate.now());
        assertEquals("V001", venta.getCodigo());
        assertEquals("2025-10-07", venta.getFecha());
        assertEquals(0, venta.getMontoTotal());
    }

    @Test
    void testAgregarDetalle() {
        Producto p1 = new Producto("Manzana", "P001", 10, 2.5, "Frutas");
        DetalleVenta detalle = new DetalleVenta(p1, 3);
        Venta venta = new Venta("V002", LocalDate.now());

        venta.agregarDetalle(detalle);
        List<DetalleVenta> detalles = venta.getDetalles();

        assertEquals(1, detalles.size());
        assertEquals(detalle, detalles.get(0));
    }

    @Test
    void testGetDetalles() {
        Venta venta = new Venta("V003", LocalDate.now());
        assertNotNull(venta.getDetalles());
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    void testCalcularMontoTotal() {
        Producto p1 = new Producto("Manzana", "P001", 10, 2.5, "Frutas");
        Producto p2 = new Producto("Leche", "P002", 5, 4.0, "Lácteos");

        DetalleVenta d1 = new DetalleVenta(p1, 2); // subtotal 5.0
        DetalleVenta d2 = new DetalleVenta(p2, 3); // subtotal 12.0

        Venta venta = new Venta("V004", LocalDate.now());
        venta.agregarDetalle(d1);
        venta.agregarDetalle(d2);

        double total = venta.calcularMontoTotal();
        assertEquals(17.0, total, 0.001);
        assertEquals(17.0, venta.getMontoTotal(), 0.001);
    }

    @Test
    void testGetMontoTotal() {
        Venta venta = new Venta("V005", LocalDate.now());
        assertEquals(0, venta.getMontoTotal());
    }

    @Test
    void testGetCodigo() {
        Venta venta = new Venta("V006", LocalDate.now());
        assertEquals("V006", venta.getCodigo());
    }

    @Test
    void testGetFecha() {
        Venta venta = new Venta("V007", LocalDate.now());
        assertEquals("2025-10-07", venta.getFecha());
    }

    @Test
    void testToString() {
        Producto p = new Producto("Arroz", "001", 10, 3.5, "Granos");
        DetalleVenta detalle = new DetalleVenta(p, 2);
        Venta venta = new Venta("V001", LocalDate.now());
        venta.agregarDetalle(detalle);

        String texto = venta.toString();

        // Aserciones básicas
        assertTrue(texto.contains("V001"));
        assertTrue(texto.contains("Arroz"));
        assertTrue(texto.contains("Detalles"));
        assertTrue(texto.contains("Monto total")); 
    }

}

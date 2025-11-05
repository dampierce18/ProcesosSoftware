package minimarket.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("Arroz", "001", 10, 3.5, "Granos");
    }

    @Test
    void testProducto() {
        Producto p = new Producto();
        assertNotNull(p);
        assertNull(p.getNombre());
        assertEquals(0, p.getStock());
    }

    @Test
    void testProductoStringStringIntDoubleString() {
        assertEquals("Arroz", producto.getNombre());
        assertEquals("001", producto.getCodigo());
        assertEquals(10, producto.getStock());
        assertEquals(3.5, producto.getPrecio(), 0.01);
        assertEquals("Granos", producto.getCategoria());
    }

    @Test
    void testGetNombre() {
        assertEquals("Arroz", producto.getNombre());
    }

    @Test
    void testSetNombre() {
        producto.setNombre("Azúcar");
        assertEquals("Azúcar", producto.getNombre());
    }

    @Test
    void testGetCodigo() {
        assertEquals("001", producto.getCodigo());
    }

    @Test
    void testSetCodigo() {
        producto.setCodigo("002");
        assertEquals("002", producto.getCodigo());
    }

    @Test
    void testGetStock() {
        assertEquals(10, producto.getStock());
    }

    @Test
    void testSetStock() {
        producto.setStock(20);
        assertEquals(20, producto.getStock());
    }

    @Test
    void testGetPrecio() {
        assertEquals(3.5, producto.getPrecio(), 0.01);
    }

    @Test
    void testSetPrecio() {
        producto.setPrecio(5.0);
        assertEquals(5.0, producto.getPrecio(), 0.01);
    }

    @Test
    void testGetCategoria() {
        assertEquals("Granos", producto.getCategoria());
    }

    @Test
    void testSetCategoria() {
        producto.setCategoria("Abarrotes");
        assertEquals("Abarrotes", producto.getCategoria());
    }

    @Test
    void testAumentarStock() {
        producto.aumentarStock(5);
        assertEquals(15, producto.getStock());
    }

    @Test
    void testReducirStock() {
        producto.reducirStock(3);
        assertEquals(7, producto.getStock());
    }

    @Test
    void testToString() {
        String result = producto.toString();
        assertTrue(result.contains("Arroz"));
        assertTrue(result.contains("001"));
        assertTrue(result.contains("Granos"));
        assertTrue(result.contains("Stock"));
        assertTrue(result.contains("Precio"));
    }
}

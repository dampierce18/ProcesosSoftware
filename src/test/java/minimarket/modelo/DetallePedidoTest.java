package minimarket.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetallePedidoTest {

    private Producto producto;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
        producto = new Producto("Arroz", "001", 10, 3.5, "Granos");
        detalle = new DetallePedido(producto, 2);
    }

    @Test
    void testConstructorInicializaCorrectamente() {
        assertEquals(producto, detalle.getProducto());
        assertEquals(2, detalle.getCantidad());
    }

    @Test
    void testSettersYGettersFuncionanCorrectamente() {
        Producto nuevo = new Producto("Azúcar", "002", 15, 2.0, "Dulces");
        detalle.setProducto(nuevo);
        detalle.setCantidad(5);

        assertEquals(nuevo, detalle.getProducto());
        assertEquals(5, detalle.getCantidad());
    }

    @Test
    void testGetSubtotalCalculaCorrectamente() {
        assertEquals(7.0, detalle.getSubtotal(), 0.001);
    }

    @Test
    void testGetSubtotalConCantidadCero() {
        detalle.setCantidad(0);
        assertEquals(0.0, detalle.getSubtotal(), 0.001);
    }

    @Test
    void testToStringFormatoCorrecto() {
        String esperado = "Arroz | Cantidad: 2 | Subtotal: S/ 7.0";
        assertEquals(esperado, detalle.toString());
    }
}
